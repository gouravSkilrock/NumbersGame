package com.skilrock.lms.web.bankMgmt.Helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.bankMgmt.beans.BankDetailsBean;
import com.skilrock.lms.web.bankMgmt.beans.BankRepDataBean;
import com.skilrock.lms.web.bankMgmt.beans.BranchDetailsBean;

public class BankHelper {
 Log logger = LogFactory.getLog(BankHelper.class);
	public boolean createBank(BankDetailsBean bankDetailsBean,UserInfoBean userBean,Timestamp creationTime){
		Connection con =null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		try{
			con =DBConnect.getConnection();
			
			String query1 ="select bank_display_name from st_lms_bank_master  where bank_display_name=? or role_id=? ";
			pstmt =con.prepareStatement(query1);
			pstmt.setString(1,bankDetailsBean.getBankName());
			pstmt.setInt(2,bankDetailsBean.getRoleId());
			logger.info("check Bank "+pstmt);
			rs=pstmt.executeQuery();
			if(rs.next()){
				logger.info("bank already exists with these details");
				return false;
			}
			con.setAutoCommit(false);
			String query="insert into st_lms_bank_master(bank_display_name,bank_full_name,bank_address1,bank_address2,country,state,city,role_id,creation_date,created_by_user_id,status) values(?,?,?,?,?,?,?,?,?,?,?)";

			
			pstmt =con.prepareStatement(query);
			pstmt.setString(1,bankDetailsBean.getBankName());
			pstmt.setString(2,bankDetailsBean.getBankFullName());
			pstmt.setString(3,bankDetailsBean.getBankAddLine1());
			pstmt.setString(4,bankDetailsBean.getBankAddLine2());
			pstmt.setString(5,bankDetailsBean.getCountry());
			pstmt.setString(6,bankDetailsBean.getState());
			pstmt.setString(7,bankDetailsBean.getCity());
			pstmt.setInt(8,bankDetailsBean.getRoleId());
			pstmt.setTimestamp(9, creationTime);
			pstmt.setInt(10, userBean.getUserId());
			pstmt.setString(11,"ACTIVE");
			logger.info("create Bank "+pstmt);
			int updated = pstmt.executeUpdate();
			logger.info("Bank  crated"+updated);
			con.commit();
			return true;
			
			
		}catch(Exception e){
			
			logger.info("ms"+e.getMessage());
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
	
		return false;
	}
	public boolean createBranch(BranchDetailsBean branchDetailsBean, int userId, Timestamp creationTime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		try {
			con = DBConnect.getConnection();
			String query1 ="select branch_display_name from st_lms_branch_master  where branch_display_name=? ";
			pstmt =con.prepareStatement(query1);
			pstmt.setString(1, branchDetailsBean.getBranchName());
			logger.info("check branch "+pstmt);
			rs=pstmt.executeQuery();
			if(rs.next()){
				logger.info("branch already exists with this name");
				return false;
			}
			String query = "INSERT INTO st_lms_branch_master(branch_display_name,branch_full_name,branch_address1,branch_address2,country,state,city,creation_date,created_by_user_id,bank_id,STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, branchDetailsBean.getBranchName());
			pstmt.setString(2, branchDetailsBean.getBranchFullName());
			pstmt.setString(3, branchDetailsBean.getBranchAddLine1());
			pstmt.setString(4, branchDetailsBean.getBranchAddLine2());
			pstmt.setString(5, branchDetailsBean.getCountry());
			pstmt.setString(6, branchDetailsBean.getState());
			pstmt.setString(7, branchDetailsBean.getCity());
			pstmt.setTimestamp(8, creationTime);
			pstmt.setInt(9, userId);
			pstmt.setInt(10, branchDetailsBean.getBankId());
			pstmt.setString(11, "ACTIVE");
			logger.info("create branch " + pstmt);
			int updated = pstmt.executeUpdate();
			logger.info("branch  crated" + updated);
			con.commit();
			return true;

			
		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
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

		return false;
	}
	public boolean assignBranch(int branchId,int userId,UserInfoBean userBean,Timestamp mapTime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			con = DBConnect.getConnection();
			String query = "select bank_id from st_lms_branch_master where branch_id=? and status=?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, branchId);
			pstmt.setString(2,"ACTIVE");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				
				query = "select * from st_lms_user_branch_mapping where user_id=? ";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, userId);
				rs1 = pstmt.executeQuery();
				con.setAutoCommit(false);
				if(rs1.next()){
					query = "update st_lms_user_branch_mapping set bank_id=?,branch_id=? where user_id=?";
					pstmt = con.prepareStatement(query);
					pstmt.setInt(1,rs.getInt("bank_id") );
					pstmt.setInt(2,branchId);
					pstmt.setInt(3, userId);
					logger.info("update user  branch query" + pstmt);
					int updated = pstmt.executeUpdate();
					logger.info("user branch updated " + updated);
					query = "insert into st_lms_user_branch_mapping_history(current_user_id,bank_id,branch_id,current_branch_id,date_changes,done_by_user_id,comment) values(?,?,?,?,?,?,?)";
					pstmt = con.prepareStatement(query);
					pstmt.setInt(1,userId);
					pstmt.setInt(2,rs.getInt("bank_id"));
					pstmt.setInt(3, branchId);
					pstmt.setInt(4, rs1.getInt("branch_id"));
					pstmt.setTimestamp(5, mapTime);
					pstmt.setInt(6,userBean.getUserId());
					pstmt.setString(7,"Update");
					logger.info("assing user to branch " + pstmt);
					updated = pstmt.executeUpdate();
					logger.info("user assign to branch" + updated);
					
					
				}else{
					
					query = "insert into st_lms_user_branch_mapping(user_id,bank_id,branch_id) values(?,?,?)";
					pstmt = con.prepareStatement(query);
					pstmt.setInt(1,userId);
					pstmt.setInt(2,rs.getInt("bank_id"));
					pstmt.setInt(3, branchId);
					logger.info("assing user to branch " + pstmt);
					int updated = pstmt.executeUpdate();
					logger.info("user assign to branch" + updated);
					
				}
				
				con.commit();

				return true;

			}

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		return false;
	}	
	public String checkBankNameAvailability(String bankName){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBConnect.getConnection();
			String query = "select bank_display_name from st_lms_bank_master where bank_display_name=?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1,bankName.trim());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				

				return "Not Available";

			}else{
				
				return "Available";
				
			}

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	
	
		return "Not Available";
	}
	public String fetchUserList(int roleId) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb  = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			String query = "select user_id,user_name from st_lms_user_master where role_id=? and isrolehead='N'";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1,roleId);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
			
				sb.append(rs.getInt("user_id")).append(":").append(rs.getString("user_name")).append("|");
			

			}

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	
	
		return sb.toString();
	
	}

	public String fetchBankList(int roleId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder bankList = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			String query = "SELECT bank_id bankId, bank_display_name bankName FROM st_lms_bank_master WHERE status='ACTIVE' ORDER BY bank_id";
			if(roleId!=1){
				query = "SELECT bank_id bankId, bank_display_name bankName FROM st_lms_bank_master WHERE status='ACTIVE'  and role_id="+roleId+" ORDER BY bank_id";
			}
			
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bankList.append(rs.getInt("bankId")).append(":").append(rs.getString("bankName")).append("|");
			}
		} catch (Exception e) {
			logger.info("Exception - " + e.getMessage());
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return bankList.toString();
	}

	public int fetchBankList(int roleId, Connection con) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
	 try {

			String query = "SELECT bank_id bankId, bank_display_name bankName FROM st_lms_bank_master WHERE status='ACTIVE'  and role_id="+ roleId + " ORDER BY bank_id";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("bankId");

			}
		} catch (Exception e) {
			logger.info("Exception - " + e.getMessage());
			e.printStackTrace();
		} 

		return 0;
	}
	public String fetchBranchList(int roleId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb  = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			String query = " ";
			if(roleId==1){
				query = " select branch_id,branch_display_name from st_lms_branch_master branchM inner join 	st_lms_bank_master bankM on branchM.bank_id=bankM.bank_id where branchM.status='ACTIVE'  and bankM.status='ACTIVE' ";
				pstmt = con.prepareStatement(query);
				
			}else {
				
				query = " select branch_id,branch_display_name from st_lms_branch_master branchM inner join 	st_lms_bank_master bankM on branchM.bank_id=bankM.bank_id where branchM.status='ACTIVE'  and bankM.status='ACTIVE' and role_id=? ";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1,roleId);
				
			}
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
			
				sb.append(rs.getInt("branch_id")).append(":").append(rs.getString("branch_display_name")).append("|");
			

			}

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	
	
		return sb.toString();
	}
	public String fetchRoleList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb  = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			String query = " select distinct rm.role_id roleId,role_name roleName from st_lms_role_master  rm left join st_lms_bank_master bankM on bankM.role_id =rm.role_id where bankM.role_id is null and is_master !='Y'";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			logger.info("role list query"+pstmt);
			while (rs.next()) {
			
				sb.append(rs.getInt("roleId")).append(":").append(rs.getString("roleName")).append("|");
			

			}

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	
	
		return sb.toString();
	}
	public String fetchBranchListForBank(int bankId) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb  = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			String query = " select branch_id,branch_display_name from st_lms_branch_master branchM inner join " +
							"	st_lms_bank_master bankM on branchM.bank_id=bankM.bank_id where branchM.status='ACTIVE'  and bankM.status='ACTIVE' and branchM.bank_id=? ";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1,bankId);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
			
				sb.append(rs.getInt("branch_id")).append(":").append(rs.getString("branch_display_name")).append("|");
			

			}

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	
	
		return sb.toString();
	
	}
	
	public String fetchUserBranchDetails(int userId) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb  = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			String query = " select branch_full_name,branch_display_name,branchM.branch_id from st_lms_branch_master branchM inner join st_lms_user_branch_mapping brm on branchM.branch_id=brm.branch_id where branchM.status='ACTIVE'  and brm.user_id=? ";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1,userId);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
			
				sb.append(rs.getString("branch_id")).append(":").append( rs.getString("branch_display_name")).append(":").append(rs.getString("branch_full_name")).append("|");
			

			}

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	
	
		return sb.toString();
	
	}
	public String fetchAgentBankList() {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder bankList = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			String query = "SELECT bank_id bankId, bank_name bankName,bank_type bankType FROM st_lms_agent_bank_master ORDER BY bank_id";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bankList.append(rs.getInt("bankId")).append(":").append(rs.getString("bankType")).append(":").append(rs.getString("bankName")).append("|");
			}
		} catch (Exception e) {
			logger.info("Exception - " + e.getMessage());
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		return bankList.toString();
	
	}
	public String fetchAgentBranchListForBank(int bankId) {


		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb  = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			String query = "SELECT branch_id branchId, branch_name branchName,branch_sort_code branchSortCode FROM st_lms_agent_branch_master where bank_id=? ORDER BY branch_id";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1,bankId);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
			
				sb.append(rs.getInt("branchId")).append(":").append(rs.getString("branchSortCode")).append(":").append(rs.getString("branchName")).append("|");
			

			}

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	
	
		return sb.toString();
	
	
	}
	public boolean saveDetails(int bankId,int branchId, int agtOrgId,String accountNbr, UserInfoBean userBean,Timestamp mapTime) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			String query = "select bank_id,branch_id,account_nbr from st_lms_agent_bank_details where agent_org_id=? ";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, agtOrgId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				
				query = "insert into st_lms_agent_bank_details_history(agent_org_id,bank_id,branch_id,account_nbr,doneBy_user_id,date_changes) values(?,?,?,?,?,?)";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1,agtOrgId);
				pstmt.setInt(2,rs.getInt("bank_id"));
				pstmt.setInt(3, rs.getInt("branch_id"));
				pstmt.setString(4, rs.getString("account_nbr"));
				pstmt.setInt(5, userBean.getUserId());
				pstmt.setTimestamp(6,mapTime);
				logger.info("save agent Bank Details history query" + pstmt);
				int inserted = pstmt.executeUpdate();
				logger.info("agent Bank Details  history saved" + inserted);
				
				query = "update  st_lms_agent_bank_details set bank_id=?,branch_id=?,account_nbr=?,doneBy_user_id=? where agent_org_id=?";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1,bankId);
				pstmt.setInt(2, branchId);
				pstmt.setString(3, accountNbr);
				pstmt.setInt(4, userBean.getUserId());
				pstmt.setInt(5,agtOrgId);
				logger.info("update agent Bank Details " + pstmt);
				int updated = pstmt.executeUpdate();
				logger.info("agent Bank Details updated" + updated);
					
				}else{
					
					query = "insert into st_lms_agent_bank_details(agent_org_id,bank_id,branch_id,account_nbr,doneBy_user_id) values(?,?,?,?,?)";
					pstmt = con.prepareStatement(query);
					pstmt.setInt(1,agtOrgId);
					pstmt.setInt(2,bankId);
					pstmt.setInt(3, branchId);
					pstmt.setString(4, accountNbr);
					pstmt.setInt(5, userBean.getUserId());
					logger.info("save agent Bank Details " + pstmt);
					int inserted = pstmt.executeUpdate();
					logger.info("agent Bank Details saved" + inserted);
					
				}
				
				con.commit();

				return true;

		

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		return false;
	
		
	}
	
	public void saveDetails(int bankId,int branchId, int agtOrgId,String accountNbr, UserInfoBean userBean,Timestamp mapTime , Connection con) throws LMSException {

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			String query = "select bank_id,branch_id,account_nbr from st_lms_agent_bank_details where agent_org_id=? ";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, agtOrgId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				query = "insert into st_lms_agent_bank_details_history(agent_org_id,bank_id,branch_id,account_nbr,doneBy_user_id,date_changes) values(?,?,?,?,?,?)";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1,agtOrgId);
				pstmt.setInt(2,rs.getInt("bank_id"));
				pstmt.setInt(3, rs.getInt("branch_id"));
				pstmt.setString(4, rs.getString("account_nbr"));
				pstmt.setInt(5, userBean.getUserId());
				pstmt.setTimestamp(6,mapTime);
				logger.info("save agent Bank Details history query" + pstmt);
				int inserted = pstmt.executeUpdate();
				logger.info("agent Bank Details  history saved" + inserted);
				
				query = "update  st_lms_agent_bank_details set bank_id=?,branch_id=?,account_nbr=?,doneBy_user_id=? where agent_org_id=?";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1,bankId);
				pstmt.setInt(2, branchId);
				pstmt.setString(3, accountNbr);
				pstmt.setInt(4, userBean.getUserId());
				pstmt.setInt(5,agtOrgId);
				logger.info("update agent Bank Details " + pstmt);
				int updated = pstmt.executeUpdate();
				logger.info("agent Bank Details updated" + updated);
			}else{
				query = "insert into st_lms_agent_bank_details(agent_org_id,bank_id,branch_id,account_nbr,doneBy_user_id) values(?,?,?,?,?)";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1,agtOrgId);
				pstmt.setInt(2,bankId);
				pstmt.setInt(3, branchId);
				pstmt.setString(4, accountNbr);
				pstmt.setInt(5, userBean.getUserId());
				logger.info("save agent Bank Details " + pstmt);
				int inserted = pstmt.executeUpdate();
				logger.info("agent Bank Details saved" + inserted);
			}
		} catch (Exception e) {
			logger.info("ms" + e.getMessage());
			e.printStackTrace();
			throw new LMSException(LMSErrors.AGENT_BANK_DETAILS_ERROR_CODE, LMSErrors.AGENT_BANK_DETAILS_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(pstmt, rs);
		} 
	}

	
	
	
	public String fetchAgentBankDetails(int agtOrgId) {


		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb  = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			String query = " select branch_name,bank_name,account_nbr from (select bank_id,branch_id,account_nbr from st_lms_agent_bank_details where agent_org_id=?)agtDet left join st_lms_agent_bank_master bm  on bm.bank_id=agtDet.bank_id   left join st_lms_agent_branch_master brm on brm.branch_id=agtDet.branch_id";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1,agtOrgId);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
			
				sb.append(rs.getString("branch_name")).append(":").append( rs.getString("bank_name")).append(":").append(rs.getString("account_nbr")).append("|");
			

			}

		} catch (Exception e) {

			logger.info("ms" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	
	
		return sb.toString();
	
	
	}
	public List<BankRepDataBean> fetchBankandBranchDetails(String delType,
			int bankId, int branchId, String regStartDate, String regEndDate,int roleId) {


		Connection con =null;
		PreparedStatement ps =null;
		ResultSet rs =null;
		List<BankRepDataBean> bankRepDataList =new ArrayList<BankRepDataBean>();
		try{
			con =DBConnect.getConnection();
			StringBuilder  queryBuilder =new StringBuilder();
			String appendQuery =" ";
		
			if(roleId!=1){
				bankId=fetchBankList(roleId,con);
				if(bankId==0){
					logger.info("Error In Getting Bank Id");
					return bankRepDataList;
				}
			}
			boolean isBank =true;
			if(!delType.equalsIgnoreCase("Bank")){
				isBank=false;
			}
			if(branchId>0){
				appendQuery=" and brm.bank_id="+bankId+" and brm.branch_id="+branchId;
				
			}else if(bankId>0){
				appendQuery=" and brm.bank_id="+bankId;
				
			}
			if(isBank){
				queryBuilder.append("select bank_id,bank_display_name,bank_full_name,sm.name,cm.city_name,creation_date,bm.status from (select bank_id,bank_display_name,bank_full_name,state,city,creation_date,status from st_lms_bank_master brm where  date(creation_date)>=? and date(creation_date)<=?  "+appendQuery+"  ) bm inner join st_lms_state_master sm on sm.state_code=bm.state inner join st_lms_city_master cm on cm.city_code=bm.city order by bank_id");
				
			}else{
				queryBuilder.append("select bank_id,branch_id,bank_display_name,bank_full_name,branch_display_name,branch_full_name,sm.name,cm.city_name,creation_date,bm.status from ( select  brm.bank_id,branch_id,bank_display_name,bank_full_name,branch_display_name,branch_full_name,brm.state,brm.city,brm.creation_date,brm.status from st_lms_branch_master brm inner join st_lms_bank_master bankm on brm.bank_id=bankm.bank_id where  date( brm.creation_date)>=? and date( brm.creation_date)<=? "+appendQuery+" ) bm inner join st_lms_state_master sm on sm.state_code=bm.state inner join st_lms_city_master cm on cm.city_code=bm.city order by bank_id");
			}
		
		
			ps =con.prepareStatement(queryBuilder.toString());
			ps.setString(1, regStartDate);
			ps.setString(2, regEndDate);
			logger.info("bank datails query"+ps);
			rs =ps.executeQuery();
			if(isBank){
				while(rs.next()){
					BankRepDataBean repDataBean = new BankRepDataBean();
					repDataBean.setBankName(rs.getString("bank_display_name"));
					repDataBean.setBankFullName(rs.getString("bank_full_name"));
					repDataBean.setState(rs.getString("name"));
					repDataBean.setCity(rs.getString("city_name"));
					repDataBean.setStatus(rs.getString("status"));
					repDataBean.setCreationDate(rs.getString("creation_date").replace(".0", ""));
					bankRepDataList.add(repDataBean);
				}
			}else{
				
				while(rs.next()){
					BankRepDataBean repDataBean = new BankRepDataBean();
					repDataBean.setBankName(rs.getString("bank_display_name"));
					repDataBean.setBankFullName(rs.getString("bank_full_name"));
					repDataBean.setBranchName(rs.getString("branch_display_name"));
					repDataBean.setBranchFullName(rs.getString("branch_full_name"));
					repDataBean.setState(rs.getString("name"));
					repDataBean.setCity(rs.getString("city_name"));
					repDataBean.setStatus(rs.getString("status"));
					repDataBean.setCreationDate(rs.getString("creation_date").replace(".0",""));
					
					bankRepDataList.add(repDataBean);
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try{
				if(con!=null){
					con.close();
				}
				if(ps!=null){
					ps.close();
				}
				if(rs!=null){
					rs.close();
				}
					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return bankRepDataList;
	
	
	}
	public List<BankRepDataBean> fetchCashierDetails(int bankId, int branchId,
			String regStartDate, String regEndDate, int roleId) {



		Connection con =null;
		PreparedStatement ps =null;
		ResultSet rs =null;
		List<BankRepDataBean> bankRepDataList =new ArrayList<BankRepDataBean>();
		try{
			con =DBConnect.getConnection();
			StringBuilder  queryBuilder =new StringBuilder();
			String appendQuery =" ";
		
			if(roleId!=1){
				bankId=fetchBankList(roleId,con);
				if(bankId==0){
					logger.info("Error In Getting Bank Id");
					return bankRepDataList;
				}
			}
			
			
			if(branchId>0){
				appendQuery=" and brmap.bank_id="+bankId+" and brmap.branch_id="+branchId;
			}else if(bankId>0){
				appendQuery=" and brmap.bank_id="+bankId;
			}
			queryBuilder.append("select brmap.user_id,registration_date,ifnull(last_login_date,'NA') last_login_date,user_name,um.status cashierStatus,bank_display_name,bank_full_name,branch_display_name,branch_full_name from  st_lms_user_branch_mapping  brmap left join st_lms_user_master um on brmap.user_id=um.user_id left join st_lms_bank_master bm on brmap.bank_id=bm.bank_id left join st_lms_branch_master brm on brmap.branch_id=brm.branch_id where date(registration_date)>=? and date(registration_date)<=? "+appendQuery+" order by brmap.bank_id"); 
			ps =con.prepareStatement(queryBuilder.toString());
			ps.setString(1, regStartDate);
			ps.setString(2, regEndDate);
			logger.info("cashier  datails query"+ps);
			rs =ps.executeQuery();
		
			while(rs.next()){
					
					BankRepDataBean repDataBean = new BankRepDataBean();
					repDataBean.setBankName(rs.getString("bank_display_name"));
					repDataBean.setBankFullName(rs.getString("bank_full_name"));
					repDataBean.setBranchName(rs.getString("branch_display_name"));
					repDataBean.setBranchFullName(rs.getString("branch_full_name"));
					repDataBean.setStatus(rs.getString("cashierStatus"));
					repDataBean.setCashierName(rs.getString("user_name"));
					repDataBean.setCashierRegDate(rs.getString("registration_date").replace(".0", ""));
					repDataBean.setCashierLastLoginDate(rs.getString("last_login_date").replace(".0", ""));
					bankRepDataList.add(repDataBean);
		
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try{
				if(con!=null){
					con.close();
				}
				if(ps!=null){
					ps.close();
				}
				if(rs!=null){
					rs.close();
				}
					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return bankRepDataList;
	
	
	
	}
	
	
}
