package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.skilrock.lms.beans.UserDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;


public class UserPrivReportHelper {
	static Log logger = LogFactory.getLog(UserPrivReportHelper.class);

	public Map<String, Map<String, String>> getServicePrivDetails() throws LMSException{
				
		Map<String, Map<String, String>> servicePrivDetailMap = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		
		try{		
				Map<String, String> serviceMap = new TreeMap<String, String>();
				Map<String, String> groupMap = new TreeMap<String, String>();
				Map<String, String> grpPrivMap = new TreeMap<String,String>();
				Map<String, String> servicePrivMap = new TreeMap<String, String>();
				Map<String, String> relatedToPrivMap = new TreeMap<String, String>();
				Set<String> privIds = new HashSet<String>();
				Set<String> headGroupNames = new HashSet<String>();
				Set<String> servicePrivIds = new HashSet<String>();
				Set<String> relatedToPrivIds = new HashSet<String>();
				
				
				String serviceId;
				String privTable = null;
				String groupName = null;
				String relatedTo = null;
				
				String fetchService = "select distinct(sm.service_id),service_display_name,priv_rep_table from st_lms_service_master sm,st_lms_service_delivery_master sdm where sm.status='ACTIVE' and sdm.status='ACTIVE' and sdm.service_id=sm.service_id ;";	
		
				con = DBConnect.getConnection();
				stmt = con.createStatement();			
				logger.debug("fetchingServicesDetails====" + fetchService);
				rs = stmt.executeQuery(fetchService);
				while(rs.next()){
					serviceId = rs.getString("service_id");
					privTable = rs.getString("priv_rep_table");
					serviceMap.put(serviceId, rs.getString("service_display_name"));				
					pstmt = con.prepareStatement("select distinct(group_name), related_to from "+rs.getString("priv_rep_table")+" where status='ACTIVE' and priv_id in (" +
							"select priv_id from st_lms_user_priv_mapping where user_id = " +
							"(select user_id from st_lms_user_master where isrolehead = 'Y' and role_id = 1) " +
							"and service_mapping_id in " +
							"(select service_delivery_master_id from st_lms_service_delivery_master where service_id = ? and interface = 'WEB' and status = 'ACTIVE')" +
							"and status = 'ACTIVE' )" +
							" and is_start = 'Y' order by related_to");						
						pstmt.setInt(1, Integer.parseInt(serviceId));
						logger.debug("fetchingGroupDetails====" + pstmt);
					rs2 = pstmt.executeQuery();
					while(rs2.next()){
						groupName = rs2.getString("group_name");
						relatedTo = rs2.getString("related_to");
						pstmt = con.prepareStatement("select priv_id from "+privTable+" where group_name = ? and related_to = ?");
							pstmt.setString(1, groupName);
							pstmt.setString(2, relatedTo);
							rs3 = pstmt.executeQuery();
							while(rs3.next()){
								privIds.add(rs3.getString("priv_id"));
								if(!servicePrivMap.containsKey(serviceId)){
									servicePrivIds.clear();
								}
								servicePrivIds.add(rs3.getString("priv_id"));
								servicePrivMap.put(serviceId, servicePrivIds.toString());	
								if(!relatedToPrivMap.containsKey(serviceId+"-"+relatedTo)){
									relatedToPrivIds.clear();
								}
								relatedToPrivIds.add(rs3.getString("priv_id"));
								relatedToPrivMap.put(serviceId+"-"+relatedTo, relatedToPrivIds.toString());
							}
							headGroupNames.add(relatedTo);
							if(!privIds.isEmpty()){
								if(relatedTo.equals("ROLE_MGT")){
									relatedTo = "BO_USER_MGT";
								}
								grpPrivMap.put(serviceId+"-"+relatedTo+"-"+groupName, privIds.toString());
								privIds.clear();
							}
					}						
							if(!headGroupNames.isEmpty()){
								headGroupNames.remove("ROLE_MGT");
								groupMap.put(serviceId, headGroupNames.toString().trim());
								headGroupNames.clear();
							}
				}
				//logger.debug("servicePrivMap: "+servicePrivMap);
				//logger.debug("relPrivMap: "+relatedToPrivMap);
				//logger.debug("serviceMap : "+serviceMap);
				//logger.debug("groupMap : "+groupMap);
				//logger.debug("GroupPrivMap : "+grpPrivMap);
			
				servicePrivDetailMap = new TreeMap<String, Map<String, String>>();
				servicePrivDetailMap.put("ServiceMap", serviceMap);
				servicePrivDetailMap.put("GroupMap", groupMap);
				servicePrivDetailMap.put("GroupPrivMap", grpPrivMap);
				servicePrivDetailMap.put("DirServPrivMap", servicePrivMap);
				servicePrivDetailMap.put("DirRelPrivMap", relatedToPrivMap);
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} catch(Exception e){
			logger.error("Exception: " + e);
			e.printStackTrace();
		}finally {		
			DBConnect.closeConnection(con, pstmt, stmt, rs);
			DBConnect.closeRs(rs2);
			DBConnect.closeRs(rs3);
		}		
		return servicePrivDetailMap;	 
	}
	
public Map<String, Map<String, String>> getTierUserDetails(int roleId, boolean isBoMaster, int loggedInUserId) throws LMSException{
		
		
		Map<String, Map<String, String>> tierUserDetailMap = new TreeMap<String, Map<String, String>>();
		String userDetails = null;
		
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String userType = null;
		
		try{
				Map<String, String> userTypeMap = new TreeMap<String, String>();
				Map<String, String> userMap = new TreeMap<String, String>();
				List<String> users = new ArrayList<String>();	
				
				String fetchTiers = "select tier_id, tier_name, tier_code from st_lms_tier_master where tier_status = 'ACTIVE' and tier_code = 'BO';";
				
				if(isBoMaster){
					userDetails = "select distinct um.user_name,um.role_id, um.user_id from st_lms_user_master as um,st_lms_user_priv_mapping as upm, (select role_id from st_lms_role_master where tier_id = 1) rm where upm.user_id=um.user_id and um.role_id = rm.role_id and um.parent_user_id <> 0 and um.status <> 'TERMINATE' order by um.user_name;";
					userTypeMap.clear();
					userTypeMap.put("AL", "All");					
					userTypeMap.put("RH", "Heads");
					userTypeMap.put("RS", "Sub Users");
				}else{
					userDetails = "select distinct um.user_name,um.role_id, um.user_id from st_lms_user_master as um ,st_lms_user_priv_mapping as upm where upm.role_id= "+roleId+" and upm.user_id=um.user_id and um.isrolehead='N' and um.status <> 'TERMINATE' order by um.user_name;";
					userTypeMap.clear();
					userTypeMap.put("RS", "Sub Users");
				}
					
				logger.debug("fetchingServicesDetails====" + fetchTiers);
				
				con = DBConnect.getConnection();
				stmt = con.createStatement();
				rs = stmt.executeQuery(fetchTiers);
				
				while(rs.next()){
					logger.debug("fetchingUserDetails====" + userDetails);
					pstmt = con.prepareStatement(userDetails);
					rs2 = pstmt.executeQuery();
					while(rs2.next()){
						userType = checkUserType(rs2.getInt("user_id"), loggedInUserId);
						users.add(rs2.getString("user_id")+"-"+rs2.getString("role_id")+"-"+userType+"-"+rs2.getString("user_name"));
					}	
					if(!users.isEmpty()){
						userMap.put(rs.getString("tier_id"), users.toString());
						users.clear();
					}
				}			
				//logger.debug("userTypeMap : "+userTypeMap);
				//logger.debug("userMap : "+userMap);		
				
				tierUserDetailMap.put("UserTypeMap", userTypeMap);
				tierUserDetailMap.put("UserMap", userMap);		
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		}catch(Exception e){
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt,stmt, rs);
			DBConnect.closeRs(rs2);
		}		
		return tierUserDetailMap;	 
	}


public Map dispSearchBoUser(int roleId, int userId) throws LMSException {
	
	Map roleMap = new TreeMap();
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	String query = null;
	try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			
			if(isBoMaster(userId)){
				query = "select role_id,role_description from st_lms_role_master where tier_id = (select tier_id from st_lms_tier_master where tier_code='BO') order by role_name";
			}else{
				query = "select role_id,role_description from st_lms_role_master where role_id = "+roleId+" and tier_id = (select tier_id from st_lms_tier_master where tier_code='BO')";
			}		
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				roleMap.put(rs.getInt("role_id"), rs.getString("role_description"));
			}
			DBConnect.closeCon(con);
		} catch (Exception e) {
				throw new LMSException(e);
		}
		return roleMap;
}

public List<UserDetailsBean> getPriviledgedUsers(String serviceId, String privStr, boolean isBoMaster, int userId) throws LMSException{

	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	UserDetailsBean userDetailsBean = null;
	String privUserFetchQuery = null;
	List<UserDetailsBean> usersDetails = new ArrayList<UserDetailsBean>();
	String genQuery = "select distinct(um.user_id), first_name, last_name, um.user_name, um.status, isrolehead, role_name, rm.role_id from st_lms_user_master um, st_lms_user_contact_details ucd, st_lms_role_master rm, (select * from st_lms_user_priv_mapping where service_mapping_id IN (select service_delivery_master_id from st_lms_service_delivery_master where service_id = '"+serviceId+"' and interface = 'WEB') and status = 'ACTIVE' and priv_id IN ("+privStr+") and role_id IN (select role_id from st_lms_role_master where tier_id = 1)) upm where um.user_id = upm.user_id and upm.user_id = ucd.user_id and um.role_id = rm.role_id and um.parent_user_id !=0 and um.status <> 'TERMINATE'";
	String otherQuery = "select distinct(um.user_id), first_name, last_name, um.user_name, um.status, isrolehead, role_name, rm.role_id from st_lms_user_master um, st_lms_user_contact_details ucd, st_lms_role_master rm, (select * from st_lms_user_priv_mapping where service_mapping_id IN (select service_delivery_master_id from st_lms_service_delivery_master where service_id = '"+serviceId+"' and interface = 'WEB') and status = 'ACTIVE'  and priv_id IN ("+privStr+") and role_id IN (select role_id from st_lms_role_master where tier_id = 1)) upm where um.user_id = upm.user_id and upm.user_id = ucd.user_id and um.role_id = rm.role_id and um.parent_user_id = "+userId+" and um.status <> 'TERMINATE'";
	
	try{			
			if(isBoMaster)
				privUserFetchQuery = "("+genQuery+" and um.role_id = 1 ) union ("+genQuery+" and um.role_id != 1 and um.isrolehead = 'Y')";
			else
				privUserFetchQuery = otherQuery;			
			logger.debug("fetchingPrivUsers====" + privUserFetchQuery);
			
			con = DBConnect.getConnection();
			stmt = con.createStatement();		
			rs = stmt.executeQuery(privUserFetchQuery);
			while(rs.next()){
				userDetailsBean = new UserDetailsBean();
				userDetailsBean.setUserId(rs.getInt("user_id"));
				userDetailsBean.setUserName(rs.getString("user_name"));
				userDetailsBean.setFirstName(rs.getString("first_name"));
				userDetailsBean.setLastName(rs.getString("last_name"));
				userDetailsBean.setStatus(rs.getString("status"));
				userDetailsBean.setDepartment(rs.getString("role_name"));
				userDetailsBean.setRoleId(Integer.parseInt(rs.getString("role_id")));
				if(rs.getString("isrolehead").equals("Y"))
					userDetailsBean.setIsRoleHead("Head");
				else
					userDetailsBean.setIsRoleHead("Not Head");	
				usersDetails.add(userDetailsBean);
			}		
			//logger.debug("UserDetails : "+usersDetails);
	}catch (SQLException e) {
		logger.error("Exception: " + e);
		e.printStackTrace();
	} catch(Exception e){
		logger.error("Exception: " + e);
		e.printStackTrace();
	}finally {
		DBConnect.closeConnection(con, stmt, rs);
	}	
	return usersDetails;		 
}



public Map<String, Map<String, List<String>>> fetchUserActivePrivs(String userId) throws LMSException{
	
	Map<String, Map<String, List<String>>> userActivePrivsMap = null;
	Map<String, List<String>> relatedToPrivGrpMap = null;
	List<String> privGroupList = null;
	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	
	int size ;
	String serviceId;
	String privRepTable;
	String relatedTo;	
	
	try{		
			String fetchServiceNameQuery = "select a.service_id, a.service_display_name, b.priv_rep_table from st_lms_service_master a, (select distinct(service_id), status, priv_rep_table from st_lms_service_delivery_master where service_delivery_master_id IN (select service_mapping_id from st_lms_user_priv_mapping where service_mapping_id IN (select service_delivery_master_id from st_lms_service_delivery_master where service_id IN (select service_id from st_lms_service_master))and user_id = "+userId+" and status = 'ACTIVE')) b where a.service_id = b.service_id and a.status='ACTIVE' and b.status='ACTIVE'";
			userActivePrivsMap = new HashMap<String, Map<String, List<String>>>();
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(fetchServiceNameQuery);
			logger.debug(fetchServiceNameQuery);
			rs1 = pstmt.executeQuery();
			while(rs1.next()){
				size = 0;
				serviceId = rs1.getString("service_id");
				privRepTable = rs1.getString("priv_rep_table");
				pstmt = con.prepareStatement("select distinct(a.related_to) from (select distinct(group_name), related_to from "+privRepTable+" where priv_id IN (select priv_id from st_lms_user_priv_mapping where service_mapping_id IN (select service_delivery_master_id from st_lms_service_delivery_master where service_id = ? ) and user_id = ? and status = 'ACTIVE')) a");
					pstmt.setInt(1, Integer.parseInt(serviceId));
					pstmt.setString(2, userId);
					logger.debug(pstmt);
				rs2 = pstmt.executeQuery();
				relatedToPrivGrpMap = new TreeMap<String, List<String>>();
				while(rs2.next()){
					relatedTo = rs2.getString("related_to");
					pstmt = con.prepareStatement("select distinct(group_name) from "+privRepTable+" where priv_id IN (select priv_id from st_lms_user_priv_mapping where service_mapping_id IN (select service_delivery_master_id from st_lms_service_delivery_master where service_id = ? )and user_id = ? and status = 'ACTIVE') and related_to = ? and is_start = 'Y'");
						pstmt.setInt(1, Integer.parseInt(serviceId));
						pstmt.setString(2, userId);
						pstmt.setString(3, relatedTo);
						logger.debug(pstmt.toString());
					rs3 = pstmt.executeQuery();
					privGroupList = new ArrayList<String>();
					while(rs3.next()){
						privGroupList.add(rs3.getString("group_name"));
					}
					//logger.debug(privGroupList);
					size = size+privGroupList.size();					
					if(!privGroupList.isEmpty()){						
						relatedToPrivGrpMap.put(relatedTo, privGroupList);
					}
				}
				//logger.debug(relatedToPrivGrpMap);					
					if(!relatedToPrivGrpMap.isEmpty()){							
							userActivePrivsMap.put(serviceId+"-"+rs1.getString("service_display_name")+"-"+size, relatedToPrivGrpMap);
					}
			}
			//logger.debug(userActivePrivsMap);
	}catch (SQLException e) {
		logger.error("Exception: " + e);
		e.printStackTrace();
	}catch(Exception e){
		logger.error("Exception: " + e);
		e.printStackTrace();
	} finally {
		DBConnect.closeConnection(con, pstmt, rs1);
		DBConnect.closeRs(rs2);
		DBConnect.closeRs(rs3);
	}	
	return userActivePrivsMap;	 
}

public boolean isUserHead(int userId) throws LMSException{
	
	Connection con = null;
	PreparedStatement pstmt1 = null;
	ResultSet rs1 = null;
	
	try{
			con = DBConnect.getConnection();
			pstmt1 = con.prepareStatement("select isrolehead from st_lms_user_master where user_id = ?");
				pstmt1.setInt(1,userId);
			rs1 = pstmt1.executeQuery();
			if(rs1.next()){
				if("Y".equals(rs1.getString("isrolehead")))
					return true;
			}
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} catch(Exception e){
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt1, rs1);
		}	
		return false;	
	}


public boolean isBoMaster(int userId) throws LMSException{
	
	Connection con = null;
	PreparedStatement pstmt1 = null;
	ResultSet rs1 = null;
	
	try{
			con = DBConnect.getConnection();
			pstmt1 = con.prepareStatement("select parent_user_id from st_lms_user_master where user_id = ?");
				pstmt1.setInt(1,userId);
			rs1 = pstmt1.executeQuery();
			while(rs1.next()){
				if(rs1.getInt("parent_user_id") == 0){
					return true;
					}
			}
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} catch(Exception e){
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt1, rs1);
		}	
		return false;	
	}


public String checkUserType(int userId, int loggedInUserId) throws LMSException{
	
	Connection con = null;
	PreparedStatement pstmt1 = null;
	ResultSet rs1 = null;
	
	try{
			con = DBConnect.getConnection();
			pstmt1 = con.prepareStatement("select parent_user_id, isrolehead from st_lms_user_master where user_id = ?");
				pstmt1.setInt(1,userId);
			rs1 = pstmt1.executeQuery();
			if(rs1.next()){
				if(isBoMaster(loggedInUserId)){
					if(loggedInUserId == rs1.getInt("parent_user_id") && rs1.getString("isrolehead").equals("N")) {
						return "RS";
					}else if(loggedInUserId == rs1.getInt("parent_user_id") && rs1.getString("isrolehead").equals("Y")){
						return "RH";
					}
				}				
			}
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} catch(Exception e){
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt1, rs1);
		}
					
		return "RS";
	}
	
}