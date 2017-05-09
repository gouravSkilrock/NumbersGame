package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.api.lms.beans.LmsUserIdMappingRequestBean;
import com.skilrock.lms.api.lms.beans.LmsUserIdMappingResponseBean;
import com.skilrock.lms.beans.AgentRegistrationBean;
import com.skilrock.lms.beans.RetailerRegistrationBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserIdMappingBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.LSControllerImpl;
import com.skilrock.lms.common.MyTextProvider;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.controller.userMgmtController.UserMgmtController;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.ServerStartUpData;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.coreEngine.userMgmt.daoImpl.MessageInboxDaoImpl;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSRequestBean;
import com.skilrock.lms.lmswrapperAPI.client.LMSServicesClient;
import com.skilrock.lms.lmswrapperAPI.client.LMSServicesPortType;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;
import com.skilrock.lms.web.bankMgmt.Helpers.BankHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.loginMgmt.AutoGenerate;
import com.skilrock.lms.web.userMgmt.common.RetailerOrgUserRegAction;
import com.skilrock.lms.wrapper.common.NotifyWrapper;
import com.skilrock.lms.wrapper.common.Wraper;
import com.skilrock.lms.wrapper.javaBeans.UserRegistrationRequestBean;
import com.skilrock.lms.wrapper.javaBeans.UserRegistrationResponseBean;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.Static;

public class OrgNUserRegHelper extends MyTextProvider {

	/*private static final String projectName = ServletActionContext
			.getServletContext().getContextPath();*/
/*	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Statement stmt = null;*/
	private static MyTextProvider textProvider = new MyTextProvider();
	static Log logger = LogFactory.getLog(OrgNUserRegHelper.class);
	private static boolean assignEmailPriviledges(int userId, String orgType,
			String email, String[] emailPrivId, Connection con)
			throws SQLException {

		// insert details into st_report_email_user_master
		String insertEmailQuery = "insert into st_lms_report_email_user_master ("
				+ " ref_user_id, organization_id, organization_type, first_name, last_name, email_id, mob_no,	registration_date, status)"
				+ " select  aa.user_id 'ref_user_id', organization_id, organization_type, first_name, last_name,	email_id, phone_nbr, CURRENT_TIMESTAMP, 'ACTIVE'"
				+ " from st_lms_user_master aa, st_lms_user_contact_details bb"
				+ " where aa.user_id=bb.user_id and aa.user_id=" + userId;
		logger.info("insertEmailQuery == " + insertEmailQuery);
		//logger.info("insertEmailQuery == " + insertEmailQuery);
		PreparedStatement pstmt = con.prepareStatement(insertEmailQuery);
		int updateRow = pstmt.executeUpdate();
		ResultSet rs  = pstmt.getGeneratedKeys();

		if (rs.next()) {
			int newUserId = rs.getInt(1);
			logger.info("insertion into st_lms_report_email_user_master table is done & user_id is == "
					+ newUserId);
			
			pstmt = con
					.prepareStatement("insert into st_lms_report_email_priv_master (user_id, email_pid, status) "
							+ "select "
							+ newUserId
							+ ", email_pid, 'INACTIVE' from st_lms_report_email_priviledge_rep where "
							+ "priv_owner='" + orgType + "' ");
			updateRow = pstmt.executeUpdate();
			logger.info("total row inserted as inactive == " + updateRow);
		/*	System.out
					.println("total row inserted as inactive == " + updateRow);*/

			if (emailPrivId != null && emailPrivId.length > 0) {
				for (String emailPid : emailPrivId) {
					pstmt = con
							.prepareStatement(" update st_lms_report_email_priv_master set status = 'ACTIVE' where user_id = ? and email_pid = ?");
					pstmt.setInt(1, newUserId);
					pstmt.setInt(2, Integer.parseInt(emailPid.trim()));
					updateRow = pstmt.executeUpdate();	
					logger.info("total row active == " + updateRow);
					///logger.info("total row active == " + updateRow);
				}
			}
			return true;
		} else {
			throw new SQLException(
					"Key is not generated for email_user_master table.");
		}

	}

public void assignDirectInventoryToRetailer(UserInfoBean userBean,RetailerRegistrationBean retailerRegistrationBean, UserInfoBean boUserBean) {
		Connection con = null ;
		PreparedStatement  pstmt =null;
		ResultSet rs =null;
		try {
			con =DBConnect.getConnection();
			if (retailerRegistrationBean.getTerminalId() != null) {
				String query = "select user_id, organization_id from st_lms_ret_offline_master where serial_number = ?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1,retailerRegistrationBean.getTerminalId());
				rs = pstmt.executeQuery();

				int retUserOrgId = 0;

				if (rs.next()) {
					retUserOrgId = rs.getInt("organization_id");
				}else{
					
					return ;
				}
				// Assign Terminal 
				updateInvForRetailerFromBO(con,retailerRegistrationBean.getModelName(), userBean,retailerRegistrationBean.getTerminalId(), retUserOrgId, boUserBean);
				// Assign Sim
				
				if(retailerRegistrationBean.getSimModelName()!=null){
					for(int i=0;i<retailerRegistrationBean.getSimModelName().length;i++){
						if(retailerRegistrationBean.getSimModelName()[i]!=null && retailerRegistrationBean.getSim()[i]!=null){
							updateInvForRetailer(con,retailerRegistrationBean.getSimModelName()[i], userBean,retailerRegistrationBean.getSim()[i], retUserOrgId);
						}
					}
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void assignInventoryToRetailer(UserInfoBean userBean,RetailerRegistrationBean retailerRegistrationBean) {
		

		Connection con = null ;//DBConnect.getConnection();
		PreparedStatement  pstmt =null;
		ResultSet rs =null;
		try {
			con =DBConnect.getConnection();
		/*	String query = "select inv_model_id, cost_to_bo from st_lms_inv_detail where current_owner_type = ? and current_owner_id = ? and serial_no = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, userBean.getUserType());
			ps.setInt(2, userBean.getUserOrgId());
			ps.setString(3, serialNo);
			ResultSet rs = ps.executeQuery();*/



		/*	if (rs.getFetchSize() > 1) {
				logger.info("one terminal id is assigned to more than one user.");
				logger.info("one terminal id is assigned to more than one user.");
				return;
			}*/
			
                                                                                     
			// -----------

		
			
			if (retailerRegistrationBean.getTerminalId() != null) {
				String query = "select user_id, organization_id from st_lms_ret_offline_master where serial_number = ?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1,retailerRegistrationBean.getTerminalId());
				rs = pstmt.executeQuery();

				//	int retUserId = 0;
				int retUserOrgId = 0;

				if (rs.next()) {
					//retUserId = rs.getInt("user_id");
					retUserOrgId = rs.getInt("organization_id");
				}else{
					
					return ;
				}
				// Assign Terminal 
				updateInvForRetailer(con,retailerRegistrationBean.getModelName(), userBean,retailerRegistrationBean.getTerminalId(), retUserOrgId);
				// Assign Sim
				
				if(retailerRegistrationBean.getSimModelName()!=null){
					
					for(int i=0;i<retailerRegistrationBean.getSimModelName().length;i++){
					
						if(retailerRegistrationBean.getSimModelName()[i]!=null && retailerRegistrationBean.getSim()[i]!=null){
						
							updateInvForRetailer(con,retailerRegistrationBean.getSimModelName()[i], userBean,retailerRegistrationBean.getSim()[i], retUserOrgId);
						
						}
					
					
					}
					
				
				}
				
			
				// terminalId = serialNo.substring(serialNo.length() - 8,
				// serialNo.length());
			}
			
			
			
			// ----------
			

		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
public void updateInvForRetailer(Connection con,String modelName,UserInfoBean userBean,String serialNo,int retUserOrgId){
	PreparedStatement  pstmt =null;
	ResultSet rs =null;
	int invModelId = 0;
	double costToBo = 0.0;
	try{
		String query = "select  model_id,cost_to_bo  from  st_lms_inv_model_master inner join st_lms_inv_status  on inv_model_id=model_id  where model_name=? and serial_no=? and current_owner_id= ?";
		pstmt =con.prepareStatement(query);
		pstmt.setString(1,modelName);
		pstmt.setString(2, serialNo);
		pstmt.setInt(3,userBean.getUserOrgId());
		rs = pstmt.executeQuery();
		if (rs.next()) {
			invModelId = rs.getInt("model_id");
			costToBo = rs.getDouble("cost_to_bo");
		}
		DBConnect.closeConnection(pstmt, rs);
		con.setAutoCommit(false);
		query = "insert into st_lms_inv_detail (user_id, user_org_type, user_org_id, inv_model_id, serial_no, date, cost_to_bo, is_new, current_owner_type, current_owner_id) values(?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);

		pstmt.setInt(1, userBean.getUserId());
		pstmt.setString(2, userBean.getUserType());
		pstmt.setInt(3, userBean.getUserOrgId());
		pstmt.setInt(4, invModelId);
		pstmt.setString(5, serialNo);
		pstmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
		pstmt.setDouble(7, costToBo);
		pstmt.setString(8, "Y");
		pstmt.setString(9, "RETAILER");
		pstmt.setInt(10, retUserOrgId);
		logger.info("inv assign: insert in inv Details"+pstmt);
		pstmt.executeUpdate();
		DBConnect.closePstmt(pstmt);
		// -------------

		query = "update st_lms_inv_status set user_id = ?, user_org_type = ?, user_org_id = ?, current_owner_type = ?, current_owner_id = ? where inv_model_id = ? and serial_no = ?";
		pstmt = con.prepareStatement(query);

		pstmt.setInt(1, userBean.getUserId());
		pstmt.setString(2, userBean.getUserType());
		pstmt.setInt(3, userBean.getUserOrgId());
		pstmt.setString(4, "RETAILER");
		pstmt.setInt(5, retUserOrgId);
		pstmt.setInt(6, invModelId);
		pstmt.setString(7, serialNo);
		logger.info("inv assign: update   inv status"+pstmt);
		pstmt.executeUpdate();
		DBConnect.closePstmt(pstmt);
		
		con.commit();
	}catch (Exception e) {
		logger.error(" Exception ",e);
	}
	
}

	public void updateInvForRetailerFromBO(Connection con,String modelName,UserInfoBean userBean,String serialNo,int retUserOrgId, UserInfoBean boInfoBean){
		PreparedStatement  pstmt =null;
		ResultSet rs =null;
		int invModelId = 0;
		double costToBo = 0.0;
		try{
			String countryDep =((String)LMSUtility.sc.getAttribute("COUNTRY_DEPLOYED")).trim();
			
			String query = "select  model_id,cost_to_bo  from  st_lms_inv_model_master inner join st_lms_inv_status  on inv_model_id=model_id  where model_name=? and serial_no=? and current_owner_id= ?";
			pstmt =con.prepareStatement(query);
			pstmt.setString(1,modelName);
			pstmt.setString(2, serialNo);
			if ("BENIN".equalsIgnoreCase(countryDep)) {
				pstmt.setInt(3,boInfoBean.getUserOrgId());
			} else {
				pstmt.setInt(3,userBean.getUserId());
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				invModelId = rs.getInt("model_id");
				costToBo = rs.getDouble("cost_to_bo");
			}
			DBConnect.closeConnection(pstmt, rs);
			con.setAutoCommit(false);
			if ("BENIN".equalsIgnoreCase(countryDep)) {
				//First assign from BO to Agent			
				query = "insert into st_lms_inv_detail (user_id, user_org_type, user_org_id, inv_model_id, serial_no, date, cost_to_bo, is_new, current_owner_type, current_owner_id) values(?,?,?,?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(query);
		
				pstmt.setInt(1, boInfoBean.getUserId());
				pstmt.setString(2, boInfoBean.getUserType());
				pstmt.setInt(3, boInfoBean.getUserOrgId());
				pstmt.setInt(4, invModelId);
				pstmt.setString(5, serialNo);
				pstmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
				pstmt.setDouble(7, costToBo);
				pstmt.setString(8, "Y");
				pstmt.setString(9, "AGENT");
				pstmt.setInt(10, userBean.getUserOrgId());
				logger.info("inv assign: insert in inv Details"+pstmt);
				pstmt.executeUpdate();
				DBConnect.closePstmt(pstmt);
			}
			
			//First assign from Agent to Retailer			
			query = "insert into st_lms_inv_detail (user_id, user_org_type, user_org_id, inv_model_id, serial_no, date, cost_to_bo, is_new, current_owner_type, current_owner_id) values(?,?,?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(query);
	
			pstmt.setInt(1, userBean.getUserId());
			pstmt.setString(2, userBean.getUserType());
			pstmt.setInt(3, userBean.getUserOrgId());
			pstmt.setInt(4, invModelId);
			pstmt.setString(5, serialNo);
			pstmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
			pstmt.setDouble(7, costToBo);
			pstmt.setString(8, "Y");
			pstmt.setString(9, "RETAILER");
			pstmt.setInt(10, retUserOrgId);
			logger.info("inv assign: insert in inv Details"+pstmt);
			pstmt.executeUpdate();
			DBConnect.closePstmt(pstmt);
			// -------------
	
			query = "update st_lms_inv_status set user_id = ?, user_org_type = ?, user_org_id = ?, current_owner_type = ?, current_owner_id = ? where inv_model_id = ? and serial_no = ?";
			pstmt = con.prepareStatement(query);
	
			pstmt.setInt(1, userBean.getUserId());
			pstmt.setString(2, userBean.getUserType());
			pstmt.setInt(3, userBean.getUserOrgId());
			pstmt.setString(4, "RETAILER");
			pstmt.setInt(5, retUserOrgId);
			pstmt.setInt(6, invModelId);
			pstmt.setString(7, serialNo);
			logger.info("inv assign: update   inv status"+pstmt);
			pstmt.executeUpdate();
			DBConnect.closePstmt(pstmt);
			
			con.commit();
		}catch (Exception e) {
			logger.error(" Exception ",e);
		}
		
	}

	public static Map<String, String> checkUniqueIdNo(String idNbr, String idType,
			Connection conn) throws LMSException {
		Connection con = conn;
		Map<String, String> errorMap = new TreeMap<String, String>();
		try {
			Statement stmt = con.createStatement();
			String organizationName = "select * from st_lms_user_contact_details where id_nbr ='"
					+ idNbr + "' and id_type='" + idType + "'";
			logger.info("Check Unique Id Number::"+organizationName);
         ///    logger.info("Check Unique Id Number::"+organizationName);
			ResultSet res = stmt.executeQuery(organizationName);
			if (res.next()) {	
				logger.info("id Number is Already exits !!");
				
			//	logger.info("id Number is Already exits !!");
				errorMap.put("idError", textProvider.getText("msg.id.nbr.already.exists"));
				errorMap.put("returnTypeError", "input");
			} else {
				errorMap.put("idError", "A");
			}

			return errorMap;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);
		}
	}

	public UserInfoBean createAgtBean(int agtOrgId) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		UserInfoBean agtInfoBean = new UserInfoBean();
		try {
			selectQuery = "select um.user_id,um.user_name,om.organization_type,om.pwt_scrap from st_lms_organization_master om,st_lms_user_master um where um.organization_id=? and om.organization_id=um.organization_id";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setInt(1, agtOrgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtInfoBean.setUserId(rs.getInt("user_id"));
				agtInfoBean.setUserType(rs.getString("organization_type"));
				agtInfoBean.setPwtSacrap(rs.getString("pwt_scrap"));
				agtInfoBean.setUserOrgId(agtOrgId);
				agtInfoBean.setUserName(rs.getString("user_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(con!=null){
					con.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
				if(rs!=null){
					rs.close();
					
				}
				
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
		}

		return agtInfoBean;
	}

	public static synchronized Map<String, String> createNewAgentOrgNUser(
			UserInfoBean userInfoBean, AgentRegistrationBean orgUserData,String loginType)
			throws LMSException {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			PreparedStatement stmt = null;
		try {
			//String agtDepositLimit = (String)context.getAttribute("agtDepositLimit");
			String agtDepositLimit =(String) LMSUtility.sc.getAttribute("agtDepositLimit");
			//String agtWithdrawalLimit = (String)context.getAttribute("agtWithdrawalLimit");
			String agtWithdrawalLimit = (String) LMSUtility.sc.getAttribute("agtWithdrawalLimit");
			String countryDep =((String)LMSUtility.sc.getAttribute("COUNTRY_DEPLOYED")).trim();
			boolean isIFU = "IFU Code".equalsIgnoreCase(orgUserData.getIdType()) && orgUserData.getIdNo() != null && !"".equalsIgnoreCase(orgUserData.getIdNo());
			con = DBConnect.getConnection();

			// check is organization and user name is already exists
			Map<String, String> errorMap = verifyOrgName(orgUserData
					.getOrgName(), orgUserData.getUserName(), con);
			Map<String, String> idErrorMap = null;
			if (orgUserData.getIdNo() != null && !"".equals(orgUserData.getIdNo())) {
				idErrorMap = checkUniqueIdNo(orgUserData
					.getIdNo(), orgUserData.getIdType(), con);
			}
			
			if (errorMap.containsKey("returnTypeError")
					|| (idErrorMap != null && idErrorMap.containsKey("returnTypeError"))) {
				if ("A".equalsIgnoreCase(errorMap.get("orgError"))) {
					errorMap.put("orgError", "");
				}
				if ("A".equalsIgnoreCase(errorMap.get("userError"))) {
					errorMap.put("userError", "");
				}
				if(idErrorMap!=null){
				errorMap.putAll(idErrorMap);
				  if ("A".equalsIgnoreCase(idErrorMap.get("idError"))) {
					errorMap.put("idError", "");
				}
				}
				
				logger.info("organization/user/Id Number is already exist = "
						+ errorMap);
				/*logger.info("organization/user/Id Number is already exist = "
								+ errorMap);*/
				errorMap.put("returnTypeError", "input");
				return errorMap;
			}

			// get the role id from database
			
			String getRoleIdQuery = QueryManager.getST3RoleId() + "'AGENT' )";
			stmt = con.prepareStatement(getRoleIdQuery);
			rs = stmt.executeQuery();
			logger.info("get the role id from database : "
					+ getRoleIdQuery);
		/*	logger.info("get the role id from database : "
					+ getRoleIdQuery);*/
			int roleId = -1;
			if (rs.next()) {
				roleId = rs.getInt(TableConstants.ROLE_ID);
			}
			rs.close();
			stmt.close();
			if (roleId == -1) {
				new LMSException("roleId is not in database");
			}

			// get the state and country code from DB
	
			String getCountryStateCode = QueryManager.getCountryAndStateCode()
					+ " where cont.name='"
					+ orgUserData.getCountry()
					+ " ' and stat.country_code=cont.country_code and stat.name='"
					+ orgUserData.getState() + "'";
			stmt = con.prepareStatement(getCountryStateCode);
			rs = stmt.executeQuery();
			String countryCode = null;
			String stateCode = null;
			int newOrgNbr=0;
			if (rs.next()) {
				countryCode = rs.getString(TableConstants.COUNTRY_CODE);
				stateCode = rs.getString(TableConstants.STATE_CODE);
				newOrgNbr = rs.getInt("no_agt_registered")+1;				
				logger.info(" countryCode = " + countryCode
						+ "  state codde = " + stateCode);
				/*logger.info(" countryCode = " + countryCode
						+ "  state codde = " + stateCode);*/
			}

			con.setAutoCommit(false);
			String newOrgQueryCode = generateAccessCodeForOrg(stateCode, newOrgNbr,orgUserData.getUserName().trim()
					.toLowerCase(), "AGENT", con,countryDep);
			// create the new organization into DB
			if (orgUserData.getAutoScrap() == null
					|| "".equalsIgnoreCase(orgUserData.getAutoScrap().trim())) {
				orgUserData.setAutoScrap("NO");
			}
			/*		String insertOrg = QueryManager.insertST3OrganizationAgent()
					+ " values ( '" + orgUserData.getOrgType() + "','"
					+ orgUserData.getOrgName() + "', '"
					+ userInfoBean.getUserOrgId() + "','"
					+ orgUserData.getAddrLine1() + "','"
					+ orgUserData.getAddrLine2() + "','"
					+ orgUserData.getCity() + "'," + orgUserData.getPin()
					+ ",'" + stateCode + "','" + countryCode + "', 0, '"
					+ orgUserData.getCreditLimit() + "','"
					+ orgUserData.getSecurity() + "','"
					+ orgUserData.getStatusorg() + "', '"
					+ orgUserData.getCreditLimit() + "','"
					+ orgUserData.getVatRegNo() + "', '"
					+ orgUserData.getAutoScrap() + "', '"
					+ orgUserData.getReconReportType() + "') ";
			
			String insertOrg = QueryManager.insertST3OrganizationAgent()
			+ " values ( '" + orgUserData.getOrgType() + "','"
			+ orgUserData.getOrgName() + "', '"
			+ userInfoBean.getUserOrgId() + "','"
			+ orgUserData.getAddrLine1() + "','"
			+ orgUserData.getAddrLine2() + "','"
			+ orgUserData.getCity() + "'," + orgUserData.getPin()
			+ ",'" + stateCode + "','" + countryCode + "', 0, '"
			+ orgUserData.getCreditLimit() + "','"
			+ orgUserData.getSecurity() + "','"
			+ orgUserData.getStatusorg() + "', '"
			+ orgUserData.getCreditLimit() + "','"
			+ orgUserData.getVatRegNo() + "', '"
			+ orgUserData.getAutoScrap() + "','"
			+ orgUserData.getReconReportType() + "') "; */
			pstmt = con.prepareStatement(QueryManager.insertST3OrganizationAgent());
			pstmt.setString(1,orgUserData.getOrgType());
			pstmt.setString(2,orgUserData.getOrgName().trim());
			pstmt.setString(3,newOrgQueryCode);// org code 
			pstmt.setInt(4,userInfoBean.getUserOrgId());
			pstmt.setString(5,orgUserData.getAddrLine1());
			pstmt.setString(6,orgUserData.getAddrLine2());
			pstmt.setString(7,orgUserData.getCity());
			pstmt.setString(8,orgUserData.getDivision());
			pstmt.setString(9,orgUserData.getArea());
			pstmt.setString(10,stateCode);
			pstmt.setString(11,countryCode); 
			pstmt.setLong(12,orgUserData.getPin());
			pstmt.setDouble(13,orgUserData.getCreditLimit());
			pstmt.setDouble(14,orgUserData.getCreditLimit());
			pstmt.setDouble(15,orgUserData.getSecurity());
			pstmt.setString(16,orgUserData.getStatusorg());
			pstmt.setDouble(17,0);// default zero
			pstmt.setString(18,orgUserData.getVatRegNo());
			pstmt.setString(19,orgUserData.getAutoScrap());
			pstmt.setString(20,orgUserData.getReconReportType());
			int orgCreateRows = pstmt.executeUpdate();
			logger.info("rows updated = " + orgCreateRows+ " ,  org creation query = " + pstmt);
			logger.info("rows updated = " + orgCreateRows
					+ " ,  Agent org creation query = " + pstmt);

			ResultSet res = pstmt.getGeneratedKeys();
			// when organization creation completed
			if (res.next()) {
				errorMap.put("orgCode",newOrgQueryCode);
				
				int genOrgId = res.getInt(1);

				insertIntoOrgGameInvoiceMethods(genOrgId, con);
				pstmt.close();
				
				// Insert into st_lms_organization_security_levy_master (For Benin)...
				if ("BENIN".equalsIgnoreCase(countryDep)) {
					String insOrgSecurityLevyDataQuery = "INSERT INTO st_lms_organization_security_levy_master (organization_id ,initial_security_deposit ,expected_security_deposit ,collected_security_deposit ,levy_cat_type) VALUES (?,?,?,?,?)";
					pstmt = con.prepareStatement(insOrgSecurityLevyDataQuery);
					pstmt.setInt(1, genOrgId);
					pstmt.setDouble(2, orgUserData.getSecurity());
					pstmt.setDouble(3, 0.0);
					pstmt.setDouble(4, orgUserData.getSecurity());
					pstmt.setString(5, isIFU ? "CAT-1" : "CAT-2");
				
					int insOrgSecurityLevyRows = pstmt.executeUpdate();
					logger.info("total rows = " + insOrgSecurityLevyRows
							+ " data inserted into st_lms_organization_security_levy_master query : "
							+ pstmt);

					pstmt.close();
				}
				
				// insert into st_organization_limit
				String insOrgLimQuery = "insert into  st_lms_oranization_limits ( organization_id , verification_limit , approval_limit , pay_limit , scrap_limit , ola_deposit_limit , ola_withdrawal_limit, max_daily_claim_amt, self_claim, other_claim, min_claim_per_ticket, max_claim_per_ticket, block_amt, block_days, block_action,levy_rate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(insOrgLimQuery);
				pstmt.setInt(1, genOrgId);
				pstmt.setDouble(2, Double.parseDouble(orgUserData.getVerLimit()
						.replace(",", "")));
				pstmt.setDouble(3, Double.parseDouble(orgUserData.getAppLimit()
						.replace(",", "")));
				pstmt.setDouble(4, Double.parseDouble(orgUserData.getPayLimit()
						.replace(",", "")));
				pstmt.setDouble(5, Double.parseDouble(orgUserData
						.getScrapLimit().replace(",", "")));
				
				pstmt.setDouble(6, Double.parseDouble(agtDepositLimit.replace(",", "")));
				pstmt.setDouble(7, Double.parseDouble(agtWithdrawalLimit.replace(",", "")));
				pstmt.setDouble(8, orgUserData.getMaxPerDayPayLimit());
				pstmt.setString(9, orgUserData.getSelfClaim());
				pstmt.setString(10, orgUserData.getOtherClaim());
				pstmt.setDouble(11, orgUserData.getMinClaimPerTicket());
				pstmt.setDouble(12, orgUserData.getMaxClaimPerTicket());
				pstmt.setDouble(13, orgUserData.getBlockAmt());
				pstmt.setInt(14, orgUserData.getBlockDays());
				pstmt.setString(15, orgUserData.getBlockAction());
				if ("BENIN".equalsIgnoreCase(countryDep))
					pstmt.setDouble(16, isIFU ? Double.parseDouble(Utility.getPropertyValue("LEVY_CAT-1_PERCENTAGE")) : Double.parseDouble(Utility.getPropertyValue("LEVY_CAT-2_PERCENTAGE")));
				else
					pstmt.setDouble(16, 0.0);
				int insOrgLimRows = pstmt.executeUpdate();
				logger.info("total rows = " + insOrgLimRows
						+ " data inserted into st_organization_limit query : "
						+ pstmt);

				pstmt.close();

				// insert in cl_xcl_update history
				/*
				Statement stmt2=con.createStatement();
				String updtCreditXtendedLimitHistory="insert into st_lms_cl_xcl_update_history(organization_id,date_time,type,amount,updated_value,total_bal_before_update) values("+ genOrgId+",'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"','CL',"+orgUserData.getCreditLimit()+","+orgUserData.getCreditLimit()+","+"0.00)";
				logger.info("inset Query for st_lms_cl_xcl_update history:"+updtCreditXtendedLimitHistory);
				stmt2.executeUpdate(updtCreditXtendedLimitHistory);
				*/
				PreparedStatement stmt2=con.prepareStatement("insert into st_lms_cl_xcl_update_history(organization_id,done_by_user_id,date_time,type,amount,updated_value,total_bal_before_update) values(?,?,?,?,?,?,?);");
				stmt2.setInt(1, genOrgId);
				stmt2.setInt(2, userInfoBean.getUserId());
				stmt2.setTimestamp(3, Util.getCurrentTimeStamp());
				stmt2.setString(4, "CL");
				stmt2.setDouble(5, orgUserData.getCreditLimit());
				stmt2.setDouble(6, orgUserData.getCreditLimit());
				stmt2.setDouble(7, 0.00);
				logger.info(stmt2);
				stmt2.executeUpdate();

				/*
				 * // insert into st_rg_org_daily_tx String
				 * insOrgRgdailyTxQuery="insert into
				 * st_lms_rg_org_daily_tx(organization_id,date) values(?,?)";
				 * pstmt = con.prepareStatement(insOrgRgdailyTxQuery);
				 * pstmt.setInt(1, genOrgId); pstmt.setDate(2, new
				 * java.sql.Date(new Date().getTime())); int
				 * insOrgRgdailytansRows = pstmt.executeUpdate();
				 * logger.info("total rows = "+insOrgRgdailytansRows+"
				 * data inserted into st_rg_org_daily_tx query : "+pstmt);
				 * 
				 * pstmt.close(); // insert into st_rg_org_weekly_tx String
				 * insOrgRgWeeklyTxQuery="insert into
				 * st_lms_rg_org_weekly_tx(organization_id,date) values(?,?)";
				 * pstmt = con.prepareStatement(insOrgRgWeeklyTxQuery);
				 * pstmt.setInt(1, genOrgId); Calendar cal
				 * =Calendar.getInstance(); cal.set(Calendar.DAY_OF_WEEK, 2);
				 * Date weekMondayDate = cal.getTime(); pstmt.setDate(2,new
				 * java.sql.Date(weekMondayDate.getTime())); int
				 * insOrgRgWeeklytansRows = pstmt.executeUpdate();
				 * logger.info("total rows = "+insOrgRgWeeklytansRows+"
				 * data inserted into st_rg_org_weekly_tx query : "+pstmt);
				 * 
				 */

				Timestamp regDate = new java.sql.Timestamp(new Date().getTime());
				// generate the auto password
				String autoPass = AutoGenerate.autoPassword();
				pstmt.close();
				// insert data into st_lms_user_master table
				String userReg = QueryManager.insertST3AgentDetails();
				/*
				 * organization_id,role_id,organization_type,auto_password
				 * ,user_name,password,status,secret_ques,secret_ans,registration_date,
				 * isrolehead,parent_user_id, register_by_id, tp_user_id
				 * */
				pstmt = con.prepareStatement(userReg);
				pstmt.setInt(1, genOrgId);
				pstmt.setInt(2, roleId);
				pstmt.setString(3, orgUserData.getOrgType());
				pstmt.setString(4, "1");
				pstmt.setString(5, orgUserData.getUserName().trim()
						.toLowerCase());
				pstmt.setString(6, MD5Encoder.encode(autoPass));
				pstmt.setString(7, orgUserData.getStatus());
				pstmt.setString(8, orgUserData.getSecQues());
				pstmt.setString(9, orgUserData.getSecAns());
				pstmt.setTimestamp(10, regDate);
				pstmt.setString(11, "Y");
				pstmt.setInt(12, userInfoBean.getUserId());
				pstmt.setInt(13, userInfoBean.getUserId());
				if(orgUserData.getTpUserId() != null){
					pstmt.setString(14, orgUserData.getTpUserId());
				}else{
					pstmt.setString(14, null);
				}
				pstmt.executeUpdate();
				logger.info("Agent user created query : " + pstmt);

				// get the generated user_id
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					int userId = rs.getInt(1);
					logger.info("AGENT user created and user id : "
							+ userId);
					logger.info("phone no " + orgUserData.getPhone());
					// insert data into st_lms_user_contact_details
					String insertContactDetail = QueryManager
							.insertST3ContactsDetails();
					pstmt = con.prepareStatement(insertContactDetail);
					pstmt.setInt(1, userId);
					pstmt.setString(2, orgUserData.getFirstName());
					pstmt.setString(3, orgUserData.getLastName());
					pstmt.setString(4, orgUserData.getEmail());
					pstmt.setLong(5, orgUserData.getPhone());
					pstmt.setString(6, orgUserData.getIdType());
					pstmt.setString(7, orgUserData.getIdNo());
					pstmt.setLong(8, orgUserData.getMobile());
					int contactDetailRows = pstmt.executeUpdate();
					logger.info("total rows update in st_lms_user_contact_details table == "
									+ contactDetailRows + " pstmt : " + pstmt);

					// insert password details into st_password_history table
					String passwordDetails = QueryManager
							.insertST3PasswordDetails();
					pstmt = con.prepareStatement(passwordDetails);
					pstmt.setInt(1, userId);
					pstmt.setString(2, MD5Encoder.encode(autoPass));
					int passHistoryRows = pstmt.executeUpdate();
					logger.info("total rows update in passHistory table == "
									+ passHistoryRows);
					pstmt.close();

					//	Insert Data In st_lms_user_ip_time_mapping Table.
					LoginTimeIPValidationHelper helper = new LoginTimeIPValidationHelper();
					helper.insertUserTimeLimitData(userId, con);

					// insert organization history into
					// st_lms_organization_master_history table
					//UserInfoBean loginUserBean = (UserInfoBean)ServletActionContext.getRequest().getSession().getAttribute("USER_INFO");
					/*String insertOrgHistory = QueryManager
							.insertST3OrganizationHistory()
							+ " values( "
							+ userInfoBean.getUserId()
							+ ", "
							+ genOrgId
							+ ", '"
							+ orgUserData.getAddrLine1()
							+ "', '"
							+ orgUserData.getAddrLine2()
							+ "', '"
							+ orgUserData.getDivision()
							+ "', '"
							+ orgUserData.getArea()
							+ "', '"
							+ orgUserData.getCity()
							+ "', "
							+ orgUserData.getPin()
							+ ", "
							+ orgUserData.getCreditLimit()
							+ ", "
							+ orgUserData.getSecurity()
							+ ",'ACTIVE_MANUAL_"+loginType+"'"
							+ ",'New Agent Organization created','"
							+ orgUserData.getStatusorg()
							+ "', '"
							+ new java.sql.Timestamp(new Date().getTime()).toString()
							+ "','"
							+orgUserData.getAutoScrap()
							+ "','"
							+ orgUserData.getReconReportType()
							+ "'"
							+ ")";
					stmt = con.prepareStatement(insertOrgHistory);
					logger.info(" rows updated and Query to update history 111:: "
									+ insertOrgHistory);
					int historyRows = stmt.executeUpdate();
					logger.info(historyRows
							+ " rows updated and Query to update history :: "
							+ insertOrgHistory);*/

					// insert into service role mapping for organization
					String serRole = "insert into st_lms_service_role_mapping (role_id,organization_id,id,status) values";
					int id[] = orgUserData.getId();
					String status[] = orgUserData.getStatusTable();
					StringBuilder values = new StringBuilder();
					for (int i = 0; i < id.length; i++) {
						values.append("(" + roleId + "," + genOrgId + ","
								+ id[i] + ",'" + (status[i].split("-"))[0]
								+ "'),");
					}
					serRole = serRole
							+ values.substring(0, values.length() - 1);
					logger.info("Service Role entry-----" + serRole);
					pstmt = con.prepareStatement(serRole);
					pstmt.executeUpdate();

					// insert into st_lms_user_priv_master
					/*
					 * String insertUserPrivQuery="insert into
					 * st_lms_user_priv_mapping select "+userId+","+roleId+",
					 * aa.pid, aa.status from st_lms_role_priv_mapping aa,
					 * priviledge_rep bb where aa.pid = bb.pid and aa.role_id =
					 * "+roleId; pstmt =
					 * con.prepareStatement(insertUserPrivQuery); int
					 * userPrivMasterRows = pstmt.executeUpdate();
					 * logger.info("total rows = "+userPrivMasterRows+"
					 * data inserted into st_user_priv_master query : "+pstmt);
					 */
					String serviceData[] = orgUserData.getStatusTable();
					String insertUserPriv = null;
					String privStatus = null;
					String tableName = null;
					logger.info("Service Id Length::" + id.length);
					for (int i = 0; i < id.length; i++) {
						privStatus = serviceData[i].split("-")[0];
						tableName = serviceData[i].split("-")[1];
						if (privStatus.equals("ACTIVE")) {
							privStatus = "rpm.status";
						} else {
							privStatus = "'INACTIVE'";
						}
						insertUserPriv = "insert into st_lms_user_priv_mapping (priv_id ,user_id , role_id, service_mapping_id, status) select "
								+ "  distinct(rpm.priv_id),"
								+ userId
								+ ","
								+ roleId
								+ ",rpm.service_mapping_id, "
								+ privStatus
								+ " from st_lms_role_priv_mapping rpm, "
								+ tableName
								+ " pr  where rpm.role_id = "
								+ roleId
								+ " and rpm.priv_id = pr.priv_id and service_mapping_id="
								+ id[i] + "";

						logger.info("insertUserPriv::" + i + " :: "
								+ insertUserPriv);
						pstmt = con.prepareStatement(insertUserPriv);
						pstmt.executeUpdate();
					}

					// assign privilege to AGENT
					assignEmailPriviledges(userId, orgUserData.getOrgType(),
							orgUserData.getEmail(), orgUserData
									.getEmailPrivId(), con);

					//Added For Agent Responsible gaming
					ResponsibleGaming.rgInsertion(userId, genOrgId , con);
					//ResponsibleGaming.rgInsertion(userId, genOrgId);
					

					//	Insert Registration Messages
					MessageInboxDaoImpl.getSingleInstance().addRegistrationMessage(userId, "AGENT", "WEB", con);

					// con.commit();
					
					if("true".equalsIgnoreCase(Utility.getPropertyValue("AGENT_BANK_MAPPING"))){
						new BankHelper().saveDetails(orgUserData.getBankId(), orgUserData.getBranchId(), genOrgId, orgUserData.getAccountNbr(), userInfoBean, regDate ,con);
					}
					
//					 userDataBean = new UserDataBean();
					if ("NIGERIA".equalsIgnoreCase(countryDep) && "YES".equalsIgnoreCase(Utility.getPropertyValue("IS_WRAPPER_ENABLED")) ) {
						UserRegistrationRequestBean userRegistrationRequestBean = new UserRegistrationRequestBean();  
						userRegistrationRequestBean.setUserName(orgUserData.getUserName());
						if ("WRAPPER".equals(orgUserData.getRegFrom()))
							userRegistrationRequestBean.setRegType("WRAPPER");
						else
							userRegistrationRequestBean.setRegType("LMS");
						NotifyWrapper notifyWrapper = new NotifyWrapper(Wraper.Activity.USER_REGISTER, userRegistrationRequestBean);
						UserRegistrationResponseBean userRegistrationResponseBean = (UserRegistrationResponseBean) notifyWrapper.asyncCall(notifyWrapper);
						
						if (userRegistrationResponseBean.getResponseCode() != 0) {
							errorMap = new HashMap<String, String>();
							errorMap.put("returnTypeError", "input");
							errorMap.put("userError", "User Name already exists !!");
							return errorMap;
						}
					}

					if("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE())){
						try {
							ServerStartUpData.updateOrgDataForSLE("AGENT" , genOrgId, con);
						} catch (Exception e) {
							logger.error("Could not update the orgDataMap for SLE....");
							e.printStackTrace();
						}
					}
					con.commit();

					/*	Send Registration Data to SLE 	*/
					if(ServicesBean.isSLE()) {
						UserDataBean dataBean = null;
						try {
							dataBean = UserMgmtController.getInstance().getUserInfo(orgUserData.getUserName().trim());
							dataBean.setCreatorUserId(userInfoBean.getUserId());
							NotifySLE notifySLE = new NotifySLE(SLE.Activity.USER_REGISTER, dataBean);
							notifySLE.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					logger.info("Agent registration completed successfully");
					//This is harcoded check for OLA to run independently.
					if(ServicesBean.isDG())
					Util.orgDataMap.put(genOrgId, ServerStartUpData.getOrgGameData(genOrgId, con, "AGENT"));
					if(ServicesBean.isIW())
						Util.iwOrgDataMap.put(genOrgId, ServerStartUpData.getOrgGameDataIW(genOrgId, con, "AGENT"));
					if(ServicesBean.isVS())
						Util.vsOrgDataMap.put(genOrgId, ServerStartUpData.getOrgGameDataVS(genOrgId, con, "AGENT"));
					//logger.info("sale variance after registration: " + Util.getSaleCommVariance(genOrgId,2));				
					
					// send mail to user is commented now because retailer is
					// not online
					if (orgUserData.getOrgType().equals("AGENT")) {
						String msgFor = "Thanks for registration to our gaming system  Your Login details are";
						String emailMsgTxt = "<html><table><tr><td>Hi "
								+ orgUserData.getFirstName()
								+ " "
								+ orgUserData.getLastName()
								+ "</td></tr><tr><td>"
								+ msgFor
								+ "</td></tr></table><table><tr><td>User Name :: </td><td>"
								+ orgUserData.getUserName().trim()
								+ "</td></tr><tr><td>password :: </td><td>"
								+ autoPass.toString()
								+ "</td></tr><tr><td>log on </td><td>"
								+ LMSFilterDispatcher.webLink + "/"
								+ LMSFilterDispatcher.mailProjName
								+ "/</td></tr></table></html>";
						MailSend mailSend = new MailSend(
								orgUserData.getEmail(), emailMsgTxt);
						mailSend.setDaemon(true);
						mailSend.start();
						// MailSend.sendMailToUser(email, autoPass,
						// userName.trim(), firstName, lastName, msgFor);
						logger.info("mail sent after commit");
					}
					insertIntoAgentRoleMapping(genOrgId, userInfoBean.getRoleId(), con);
					con.commit();
					return errorMap;
				} else {
					logger.info("User is not created ");
					throw new LMSException("User is not created ");
				}
			} else {
				logger.info("organization is not created ");
				throw new LMSException("Organization is not created ");
			}
		} catch (SQLException se) { 
			se.printStackTrace();
			throw new LMSException("sql exception", se);
		} finally {
			DBConnect.closeCon(con);

			//	Licensing Server Validation
			LSControllerImpl.getInstance().clientValidation();
		}
	}

	private static void insertIntoOrgGameInvoiceMethods(int orgId,
			Connection con) {

		Statement st = null ;
		PreparedStatement ps = null ;
		ResultSet rs = null ;
		
		try{
			String fetchFromGameMaster = "select game_id, invoice_scheme_id, scheme_value from st_se_game_master gm inner join st_se_invoicing_methods im on gm.invoice_scheme_id = im.invoice_method_id" ;
			String insertIntoOrgGameInvoiceMethods = "insert into st_se_org_game_invoice_methods values (?, ?, ?, ?)" ;
			st = con.createStatement() ;
			
			rs = st.executeQuery(fetchFromGameMaster) ;
			ps = con.prepareStatement(insertIntoOrgGameInvoiceMethods) ;
			
			while (rs.next()){
				ps.setInt(1, orgId) ;
				ps.setInt(2, rs.getInt("game_id"));
				ps.setInt(3, rs.getInt("invoice_scheme_id"));
				ps.setString(4, rs.getString("scheme_value"));
				ps.addBatch();
			}
			ps.executeBatch() ;
			
		}catch(Exception e){
			e.printStackTrace() ;
		}finally{
			DBConnect.closeResource(st, rs, ps) ;
		}
		
		
	}

	public Map<String, String> createNewQuickRetailerOrgNUser(
			UserInfoBean userInfoBean, RetailerOrgUserRegAction orgUserData,
			Set keySet) throws LMSException {
		String deviceType = null;
		String terminalId = null;
		Connection con =null;
		PreparedStatement pstmt=null;
		try {
			con = DBConnect.getConnection();

			// check is organization and user name is already exists
			Map<String, String> errorMap = verifyOrgName(orgUserData
					.getOrgName(), orgUserData.getUserName(), con);
			Map<String, String> idErrorMap = checkUniqueIdNo(orgUserData
					.getIdNo(), orgUserData.getIdType(), con);
			if (errorMap.containsKey("returnTypeError")
					|| idErrorMap.containsKey("returnTypeError")) {
				if ("A".equalsIgnoreCase(errorMap.get("orgError"))) {
					errorMap.put("orgError", "");
				}
				if ("A".equalsIgnoreCase(errorMap.get("userError"))) {
					errorMap.put("userError", "");
				}
				errorMap.putAll(idErrorMap);
				if ("A".equalsIgnoreCase(idErrorMap.get("idError"))) {
					errorMap.put("idError", "");
				}

				logger.info("organization/user/Id Number is already exist = "
								+ errorMap);
				errorMap.put("returnTypeError", "input");
				return errorMap;
			}

			// get the role id from database
			Statement stmt = con.createStatement();
			String getRoleIdQuery = QueryManager.getST3RoleId() + "'Retailer')";
			ResultSet rs = stmt.executeQuery(getRoleIdQuery);
			logger.info("get the role id from database : "
					+ getRoleIdQuery);
			int roleId = -1;
			if (rs.next()) {
				roleId = rs.getInt(TableConstants.ROLE_ID);
			}
			rs.close();
			stmt.close();
			if (roleId == -1) {
				new LMSException("roleId is not in database");
			}

			String orgStatus = "ACTIVE";
			if (orgUserData.getCreditLimit() <= 0.0) {
				orgStatus = "INACTIVE";
			}

			con.setAutoCommit(false);

			// create the new organization into DB
			String insertOrg = QueryManager.insertST3OrganizationAgent()
					+ " select 'RETAILER', '"
					+ orgUserData.getOrgName()
					+ "', '"
					+ userInfoBean.getUserOrgId()
					+ "',addr_line1, addr_line2, city, pin_code,"
					+ "state_code, country_code, 0, '"
					+ orgUserData.getCreditLimit()
					+ "', 0, '"
					+ orgStatus
					+ "', '"
					+ orgUserData.getCreditLimit()
					+ "',vat_registration_nbr, 'NO', 'Ticket Wise Report' from st_lms_organization_master where organization_id ="
					+ userInfoBean.getUserOrgId();
			pstmt = con.prepareStatement(insertOrg);
			logger.info("org creation query = " + pstmt);
			int orgCreateRows = pstmt.executeUpdate();
			logger.info("rows updated = " + orgCreateRows);

			ResultSet res = pstmt.getGeneratedKeys();
			// when organization creation completed
			if (res.next()) {
				int genOrgId = res.getInt(1);

				pstmt.close();
				// insert into st_organization_limit
				String insOrgLimQuery = "insert into  st_lms_oranization_limits ( organization_id , verification_limit , approval_limit , pay_limit , scrap_limit , ola_deposit_limit , ola_withdrawl_limit) values ( ?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(insOrgLimQuery);
				pstmt.setInt(1, genOrgId);
				pstmt.setDouble(2, Double.parseDouble(orgUserData.getVerLimit()
						.replace(",", "")));
				pstmt.setDouble(3, Double.parseDouble(orgUserData.getAppLimit()
						.replace(",", "")));
				pstmt.setDouble(4, Double.parseDouble(orgUserData.getPayLimit()
						.replace(",", "")));
				pstmt.setDouble(5, Double.parseDouble(orgUserData
						.getScrapLimit().replace(",", "")));
				pstmt.setDouble(6, Double.parseDouble(orgUserData
						.getOlaDepositLimit().replace(",", "")));
				pstmt.setDouble(7, Double.parseDouble(orgUserData
						.getOlaWithdrawalLimit().replace(",", "")));
				int insOrgLimRows = pstmt.executeUpdate();
				logger.info("total rows = "
								+ insOrgLimRows
								+ " data inserted into st_lms_organization_limit query : "
								+ pstmt);

				pstmt.close();

				/*
				 * // insert into st_lms_rg_org_daily_tx String
				 * insOrgRgdailyTxQuery="insert into
				 * st_lms_rg_org_daily_tx(organization_id,date) values(?,?)";
				 * pstmt = con.prepareStatement(insOrgRgdailyTxQuery);
				 * pstmt.setInt(1, genOrgId); pstmt.setDate(2, new
				 * java.sql.Date(new Date().getTime())); int
				 * insOrgRgdailytansRows = pstmt.executeUpdate();
				 * logger.info("total rows = "+insOrgRgdailytansRows+"
				 * data inserted into st_lms_rg_org_daily_tx query : "+pstmt);
				 * 
				 * pstmt.close(); // insert into st_lms_rg_org_weekly_tx String
				 * insOrgRgWeeklyTxQuery="insert into
				 * st_lms_rg_org_weekly_tx(organization_id,date) values(?,?)";
				 * pstmt = con.prepareStatement(insOrgRgWeeklyTxQuery);
				 * pstmt.setInt(1, genOrgId); Calendar cal
				 * =Calendar.getInstance(); cal.set(Calendar.DAY_OF_WEEK, 2);
				 * Date weekMondayDate = cal.getTime(); pstmt.setDate(2,new
				 * java.sql.Date(weekMondayDate.getTime())); int
				 * insOrgRgWeeklytansRows = pstmt.executeUpdate();
				 * logger.info("total rows = "+insOrgRgWeeklytansRows+"
				 * data inserted into st_lms_rg_org_weekly_tx query : "+pstmt);
				 * 
				 */

				// generate the auto password
				String autoPass = null;
				logger.info("Get Attribute: "
						+ (String) LMSUtility.sc
								.getAttribute("RETAILER_PASS"));
				if ("integer".equalsIgnoreCase(((String) LMSUtility.sc.getAttribute("RETAILER_PASS"))
						.trim())) {
					autoPass = AutoGenerate.autoPasswordInt() + "";
				} else {
					autoPass = AutoGenerate.autoPassword();
				}
				logger.info("Password Generated: " + autoPass);
				pstmt.close();
				// insert data into st_lms_user_master table
				String userReg = "INSERT into st_lms_user_master(organization_id, role_id,parent_user_id,organization_type,auto_password,user_name, "
						+ "password, status, secret_ques, secret_ans, registration_date, isrolehead) select "
						+ genOrgId
						+ ", "
						+ roleId
						+ ",'"
						+ userInfoBean.getUserId()
						+ "', 'RETAILER', '1', '"
						+ orgUserData.getUserName().trim().toLowerCase()
						+ "', '"
						+ MD5Encoder.encode(autoPass)
						+ "','ACTIVE', secret_ques, secret_ans, '"
						+ new java.sql.Timestamp(new Date().getTime())
						+ "', 'Y' from st_lms_user_master where user_id ="
						+ userInfoBean.getUserId();
				pstmt = con.prepareStatement(userReg);
				logger.info("bo user created query : " + pstmt);
				pstmt.executeUpdate();

				// get the generated user_id
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					int userId = rs.getInt(1);
					logger.info("bo user created and user id : "
							+ userId);

					// insert data into st_lms_user_contact_details
					String insertContactDetail = "INSERT into st_lms_user_contact_details(user_id,first_name,last_name,email_id,phone_nbr,"
							+ " id_type, id_nbr) select ?, ?, ?, email_id, ?, ?, ? from st_lms_user_contact_details where user_id ="
							+ userInfoBean.getUserId();
					pstmt = con.prepareStatement(insertContactDetail);
					pstmt.setInt(1, userId);
					pstmt.setString(2, orgUserData.getFirstName());
					pstmt.setString(3, orgUserData.getLastName());
					pstmt.setLong(4, orgUserData.getPhone());
					pstmt.setString(5, orgUserData.getIdType());
					pstmt.setString(6, orgUserData.getIdNo());
					int contactDetailRows = pstmt.executeUpdate();
					logger.info("total rows update in st_lms_user_contact_details table == "
									+ contactDetailRows + " pstmt : " + pstmt);

					// insert password details into st_lms_password_history
					// table
					String passwordDetails = QueryManager
							.insertST3PasswordDetails();
					pstmt = con.prepareStatement(passwordDetails);
					pstmt.setInt(1, userId);
					pstmt.setString(2, MD5Encoder.encode(autoPass));
					int passHistoryRows = pstmt.executeUpdate();
					logger.info("total rows update in passHistory table == "
									+ passHistoryRows);
					pstmt.close();

					// insert organization history into
					// st_lms_organization_master_history table
					//UserInfoBean loginUserBean = (UserInfoBean)ServletActionContext.getRequest().getSession().getAttribute("USER_INFO");
					/*String insertOrgHistory = QueryManager
							.insertST3OrganizationHistory()
							+ " values( "
							+ userInfoBean.getUserId()
							+ ", "
							+ genOrgId
							+ ", '"
							+ orgUserData.getAddrLine1()
							+ "', '"
							+ orgUserData.getAddrLine2()
							+ "', '"
							+ orgUserData.getCity()
							+ "', "
							+ orgUserData.getPin()
							+ ", "
							+ orgUserData.getCreditLimit()
							+ ", "
							+ orgUserData.getSecurity()
							+ ",'ACTIVE_MANUAL_"+userInfoBean.getUserType()+"'"
							+ ",'New Retailer Organization created','"
							+ orgUserData.getStatusorg()
							+ "', '"
							+ new java.sql.Timestamp(new Date().getTime()).toString()
							+ "','"
							+orgUserData.getAutoScrap()
							+ "','"
							+ orgUserData.getReconReportType()
							+ "'"
							+ ")";
					stmt = con.createStatement();
					int historyRows = stmt.executeUpdate(insertOrgHistory);
					logger.info(historyRows
							+ " rows updated and Query to update history :: "
							+ insertOrgHistory);*/

					// insert into service role mapping for organization Vaibhav
					String serRole = "insert into st_lms_service_role_mapping select service_delivery_master_id,"
							+ roleId
							+ ","
							+ genOrgId
							+ ",status from st_lms_service_delivery_master where tier_id in (select tier_id from st_lms_tier_master where tier_code='Retailer')";
					pstmt = con.prepareStatement(serRole);
					logger.info("query ==" + serRole);
					int orgSerRole = pstmt.executeUpdate();
					logger.info("total rows = "
									+ orgSerRole
									+ " data inserted into st_lms_user_priv_master query : "
									+ pstmt);

					// insert into st_lms_st_lms_user_priv_mapping
					String insertUserPrivQuery = "insert into st_lms_user_priv_mapping select "
							+ userId
							+ ","
							+ roleId
							+ ",priv_id,service_mapping_id,status from st_lms_role_priv_mapping where role_id="
							+ roleId;
					pstmt = con.prepareStatement(insertUserPrivQuery);
					logger.info("query ==" + insertUserPrivQuery);
					int userPrivMasterRows = pstmt.executeUpdate();
					logger.info("total rows = "
									+ userPrivMasterRows
									+ " data inserted into st_lms_user_priv_master query : "
									+ pstmt);

					// added for offline sale
					String isRetailerOffline = (String) LMSUtility.sc.getAttribute("RET_OFFLINE");
					String isLoginBind = (String) LMSUtility.sc.getAttribute("LOGIN_BINDING");
					boolean  isSimBind ="YES".equalsIgnoreCase((String) LMSUtility.sc.getAttribute("sim_binding"))?true:false;
					int retOfflineId = 0;
					StringBuilder sb = new StringBuilder();
					String address = null;
					String latLon = null;
					//if("YES".equalsIgnoreCase(isLoginBind.trim())){
						if (!"YES".equalsIgnoreCase(isRetailerOffline.trim())) {
							orgUserData.setIsOffLine("NO");
						}
						DrawGameOfflineHelper helper = new DrawGameOfflineHelper();
						address = sb.append(orgUserData.getAddrLine1()).append(",").append(orgUserData.getAddrLine2()).append(" ").append(orgUserData.getCity()).append(" ").append(orgUserData.getCountry()).toString();
						
						latLon = CommonFunctionsHelper.getLongitudeLatitude(address);
						if(orgUserData.getTerminalId() == null){
							deviceType = "-1";
							terminalId = "-1";
						} else{
							deviceType = orgUserData.getTerminalId().split("-")[0];
							terminalId = orgUserData.getTerminalId().split("-")[1];
						}
					/*	retOfflineId = helper.saveOfflineRetData(userId,
								genOrgId, orgUserData.getIsOffLine(),
								deviceType,terminalId, con,latLon,orgUserData.getCity(),isLoginBind);*/
						
						
						retOfflineId = helper.saveOfflineRetData(userId,genOrgId, orgUserData.getIsOffLine(),orgUserData.getModelName(),orgUserData.getTerminalId(), con,latLon,orgUserData.getCity(),isLoginBind,orgUserData.getSim(),orgUserData.getSimModelName(),isSimBind);
						if (retOfflineId < 1) {
							throw new LMSException("User is not created ");
						}
					//}
					
					// rg calling
					//ResponsibleGaming.rgInsertion(userId, genOrgId);
					ResponsibleGaming.rgInsertion(userId, genOrgId , con);

					con.commit();
					logger.info("retailer registration completed successfully");
					// send mail to user is commented now because retailer is
					// not online
					String isRetaileOnline = (String) LMSUtility.sc.getAttribute("RET_ONLINE");
					errorMap.put("NewPassword", autoPass);
					if ("YES".equalsIgnoreCase(isRetaileOnline.trim())) {
						String emailAddress = "";
						String msgFor = "Thanks for registration to our gaming system  Your Login details are";
						String emailMsgTxt = "<html><table><tr><td>Hi "
								+ orgUserData.getFirstName()
								+ " "
								+ orgUserData.getLastName()
								+ "</td></tr><tr><td>"
								+ msgFor
								+ "</td></tr></table><table><tr><td>User Name :: </td><td>"
								+ orgUserData.getUserName().trim()
								+ "</td></tr><tr><td>password :: </td><td>"
								+ autoPass.toString()
								+ "</td></tr><tr><td>log on </td><td>"
								+ LMSFilterDispatcher.webLink + "/"
								+ LMSFilterDispatcher.mailProjName
								+ "/</td></tr></table></html>";
						PreparedStatement stmt1 = con
								.prepareStatement("select email_id from st_lms_user_contact_details where user_id = "
										+ userInfoBean.getUserId());
						rs = stmt1.executeQuery();
						if (rs.next()) {
							emailAddress = rs.getString(1);
						}

						logger.info("email address is " + emailAddress);
						MailSend mailSend = new MailSend(emailAddress,
								emailMsgTxt);
						mailSend.setDaemon(true);
						mailSend.start();
						// MailSend.sendMailToUser(email, autoPass,
						// userName.trim(), firstName, lastName, msgFor);
						logger.info("mail sent after commit");
						stmt1 = null;
					}
					return errorMap;
				} else {
					logger.info("User is not created ");
					throw new LMSException("User is not created ");
				}
			} else {
				logger.info("organization is not created ");
				throw new LMSException("Organization is not created ");
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("sql exception", se);
		} finally {
			DBConnect.closeCon(con);
			
			//	Licensing Server Validation
			LSControllerImpl.getInstance().clientValidation();
		}

	}

	public static synchronized Map<String, String>  createNewRetailerOrgNUser(
			UserInfoBean userInfoBean, RetailerRegistrationBean orgUserData,String loginType)
			throws LMSException {
		Connection con =null;
		PreparedStatement stmt =null;
		ResultSet rs =null;
		PreparedStatement pstmt =null;
		try {
		
			String retDepositLimit = (String)LMSUtility.sc.getAttribute("retDepositLimit");
			String retWithdrawalLimit = (String)LMSUtility.sc.getAttribute("retWithdrawalLimit");
			String rootPath = LMSUtility.sc.getRealPath("/").toString();
			String countryDep =((String)LMSUtility.sc.getAttribute("COUNTRY_DEPLOYED")).trim();
			boolean isIFU = "IFU Code".equalsIgnoreCase(orgUserData.getIdType()) && orgUserData.getIdNo() != null && !"".equalsIgnoreCase(orgUserData.getIdNo()); 
			con = DBConnect.getConnection();
			
			// check is organization and user name is already exists
			Map<String, String> errorMap = verifyOrgName(orgUserData
					.getOrgName(), orgUserData.getUserName(), con);
			Map<String, String> idErrorMap = null;
			if (orgUserData.getIdNo() != null && !"".equals(orgUserData.getIdNo())) {
				idErrorMap = checkUniqueIdNo(orgUserData
					.getIdNo(), orgUserData.getIdType(), con);
			}
			
			if (errorMap.containsKey("returnTypeError")
					|| (idErrorMap != null && idErrorMap.containsKey("returnTypeError"))) {
				if ("A".equalsIgnoreCase(errorMap.get("orgError"))) {
					errorMap.put("orgError", "");
				}
				if ("A".equalsIgnoreCase(errorMap.get("userError"))) {
					errorMap.put("userError", "");
				}
				if(idErrorMap!=null){
				errorMap.putAll(idErrorMap);
				if ("A".equalsIgnoreCase(idErrorMap.get("idError"))) {
					errorMap.put("idError", "");
				}
				}

				logger.info("organization/user/Id Number is already exist = "
								+ errorMap);
				errorMap.put("returnTypeError", "input");
				return errorMap;
			}

			
			//here append the code for creating affiliate in PlayTech System
			String walletName="RUMMY"; //Default for rummy retailer reg 
			//here check for playtech retailer reg
			if((LMSFilterDispatcher.getIsOLA()).equalsIgnoreCase("YES") && "PLAY_TECH".equalsIgnoreCase(walletName))
			{
			boolean isSuccess = false;
			isSuccess = OLAUtility.createAffiliate(orgUserData, errorMap,rootPath);
			if(!isSuccess)
				return errorMap;
			}
			// get the role id from database
			String getRoleIdQuery = QueryManager.getST3RoleId()+ " 'RETAILER' )";
			stmt = con.prepareStatement(getRoleIdQuery);
			rs = stmt.executeQuery();
			logger.info("get the role id from database : "
					+ getRoleIdQuery);
			int roleId = -1;
			if (rs.next()) {
				roleId = rs.getInt(TableConstants.ROLE_ID);
			}
			rs.close();
			stmt.close();
			if (roleId == -1) {
				throw new LMSException(textProvider.getText("msg.roleId.not.inDB"));
			}

			// get the state and country code from DB
			
			String getCountryStateCode = QueryManager.getCountryAndStateCode()
					+ " where cont.name='"
					+ orgUserData.getCountry()
					+ " ' and stat.country_code=cont.country_code and stat.name='"
					+ orgUserData.getState() + "'";
			stmt = con.prepareStatement(getCountryStateCode);
			rs = stmt.executeQuery();
			String countryCode = null;
			String stateCode = null;
			int newOrgNbr=0;
			if (rs.next()) {
				countryCode = rs.getString(TableConstants.COUNTRY_CODE);
				stateCode = rs.getString(TableConstants.STATE_CODE);
				newOrgNbr = rs.getInt("no_ret_registered")+1;	
				logger.info(" countryCode = " + countryCode
						+ "  state codde = " + stateCode);
			}
			con.setAutoCommit(false);
			if(newOrgNbr<0){
				logger.info("");
				throw new LMSException(textProvider.getText("msg.error.inOrgCodeGen"));
			}
			
			String newOrgQueryCode = generateAccessCodeForOrg(stateCode, newOrgNbr,userInfoBean.getUserName().trim(), "RETAILER", con,countryDep);
			// create the new organization into DB
			if (orgUserData.getAutoScrap() == null
					|| "".equalsIgnoreCase(orgUserData.getAutoScrap().trim())
					|| !"YES".equalsIgnoreCase(userInfoBean.getPwtSacrap())) {
				orgUserData.setAutoScrap("NO");
			}
		/*	String insertOrg = QueryManager.insertST3OrganizationAgent()
					+ " values ( '" + orgUserData.getOrgType() + "','"
					+ orgUserData.getOrgName() + "', '"
					+ userInfoBean.getUserOrgId() + "','"
					+ orgUserData.getAddrLine1() + "','"
					+ orgUserData.getAddrLine2() + "','"
					+ orgUserData.getCity() + "'," + orgUserData.getPin()
					+ ",'" + stateCode + "','" + countryCode + "', 0, '"
					+ orgUserData.getCreditLimit() + "','"
					+ orgUserData.getSecurity() + "','"
					+ orgUserData.getStatusorg() + "', '"
					+ orgUserData.getCreditLimit() + "','"
					+ orgUserData.getVatRegNo() + "', '"
					+ orgUserData.getAutoScrap() + "','"
					+ orgUserData.getReconReportType() + "') ";*/
			pstmt = con.prepareStatement(QueryManager.insertST3OrganizationAgent());
			pstmt.setString(1,orgUserData.getOrgType());
			pstmt.setString(2,orgUserData.getOrgName().trim());
			pstmt.setString(3,newOrgQueryCode);// org code 
			pstmt.setInt(4,userInfoBean.getUserOrgId());
			pstmt.setString(5,orgUserData.getAddrLine1());
			pstmt.setString(6,orgUserData.getAddrLine2());
			pstmt.setString(7,orgUserData.getCity());
			pstmt.setString(8,orgUserData.getDivision());
			pstmt.setString(9,orgUserData.getArea());
			pstmt.setString(10,stateCode);
			pstmt.setString(11,countryCode); 
			pstmt.setLong(12,orgUserData.getPin());
			pstmt.setDouble(13,orgUserData.getCreditLimit());
			pstmt.setDouble(14,orgUserData.getCreditLimit());
			pstmt.setDouble(15,orgUserData.getSecurity());
			pstmt.setString(16,orgUserData.getStatusorg());
			pstmt.setDouble(17,0);// default zero
			pstmt.setString(18,orgUserData.getVatRegNo());
			pstmt.setString(19,orgUserData.getAutoScrap());
			pstmt.setString(20,orgUserData.getReconReportType());
			int orgCreateRows = pstmt.executeUpdate();
			logger.info("rows updated = " + orgCreateRows+ " ,  org creation query = " + pstmt);
	/*		logger.info("rows updated = " + orgCreateRows
					+ " ,  org creation query = " + pstmt);*/

			ResultSet res = pstmt.getGeneratedKeys();
			// when organization creation completed
			if (res.next()) {
	
				errorMap.put("orgCode",newOrgQueryCode);
				int genOrgId = res.getInt(1);

				insertIntoOrgGameInvoiceMethods(genOrgId, con);
				pstmt.close();
				
				// Insert into st_lms_organization_security_levy_master (For Benin)...
				if ("BENIN".equalsIgnoreCase(countryDep)) {
					String insOrgSecurityLevyDataQuery = "INSERT INTO st_lms_organization_security_levy_master (organization_id ,initial_security_deposit ,expected_security_deposit ,collected_security_deposit ,levy_cat_type) VALUES (?,?,?,?,?)";
					pstmt = con.prepareStatement(insOrgSecurityLevyDataQuery);
					pstmt.setInt(1, genOrgId);
					pstmt.setDouble(2, orgUserData.getSecurity());
					pstmt.setDouble(3, Double.parseDouble(Utility.getPropertyValue("SECURITY_DEPOSIT_AMOUNT")));
					pstmt.setDouble(4, orgUserData.getSecurity());
					pstmt.setString(5, isIFU ? "CAT-1" : "CAT-2");
				
					int insOrgSecurityLevyRows = pstmt.executeUpdate();
					logger.info("total rows = " + insOrgSecurityLevyRows
							+ " data inserted into st_lms_organization_security_levy_master query : "
							+ pstmt);

					pstmt.close();
				}
				
				// insert into st_organization_limit
				String insOrgLimQuery = "insert into  st_lms_oranization_limits ( organization_id , verification_limit , approval_limit , pay_limit , scrap_limit , ola_deposit_limit , ola_withdrawal_limit, max_daily_claim_amt, self_claim, other_claim, min_claim_per_ticket, max_claim_per_ticket, block_amt, block_days, block_action,levy_rate,security_deposit_rate) values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(insOrgLimQuery);
				pstmt.setInt(1, genOrgId);
				pstmt.setDouble(2, Double.parseDouble(orgUserData.getVerLimit()
						.replace(",", "")));
				pstmt.setDouble(3, Double.parseDouble(orgUserData.getAppLimit()
						.replace(",", "")));
				pstmt.setDouble(4, Double.parseDouble(orgUserData.getPayLimit()
						.replace(",", "")));
				pstmt.setDouble(5, Double.parseDouble(orgUserData
						.getScrapLimit().replace(",", "")));
			
				pstmt.setDouble(6, Double.parseDouble(retDepositLimit.replace(",", "")));
				pstmt.setDouble(7, Double.parseDouble(retWithdrawalLimit.replace(",", "")));
				pstmt.setDouble(8, orgUserData.getMaxPerDayPayLimit());
				pstmt.setString(9, orgUserData.getSelfClaim());
				pstmt.setString(10, orgUserData.getOtherClaim());
				pstmt.setDouble(11, orgUserData.getMinClaimPerTicket());
				pstmt.setDouble(12, orgUserData.getMaxClaimPerTicket());
				pstmt.setDouble(13, orgUserData.getBlockAmt());
				pstmt.setInt(14, orgUserData.getBlockDays());
				pstmt.setString(15, orgUserData.getBlockAction());
				if ("BENIN".equalsIgnoreCase(countryDep)) {
					pstmt.setDouble(16, isIFU ? Double.parseDouble(Utility.getPropertyValue("LEVY_CAT-1_PERCENTAGE")) : Double.parseDouble(Utility.getPropertyValue("LEVY_CAT-2_PERCENTAGE")));
					pstmt.setDouble(17, Double.parseDouble(Utility.getPropertyValue("SECURITY_DEPOSIT_AMOUNT")) <= orgUserData.getSecurity() ? 0 : Double.parseDouble(Utility.getPropertyValue("SECURITY_DEPOSIT_RATE")));
				} else {
					pstmt.setDouble(16, 0.0);
					pstmt.setDouble(17, 0.0);
				}
				int insOrgLimRows = pstmt.executeUpdate();
				logger.info("total rows = " + insOrgLimRows
						+ " data inserted into st_organization_limit query : "
						+ pstmt);

				pstmt.close();

				String insertRetSoldQuery = "INSERT INTO st_lms_ret_sold_claim_criteria (organization_id, claim_at_self_ret, claim_at_self_agt, claim_at_other_ret_same_agt, claim_at_other_ret, claim_at_other_agt, claim_at_bo) VALUES (?,?,?,?,?,?,?);";
				pstmt = con.prepareStatement(insertRetSoldQuery);
				pstmt.setInt(1, genOrgId);
				pstmt.setString(2, Utility.getPropertyValue("CLAIM_AT_SELF_RET"));
				pstmt.setString(3, Utility.getPropertyValue("CLAIM_AT_SELF_AGT"));
				pstmt.setString(4, Utility.getPropertyValue("CLAIM_AT_OTHER_RET_SAME_AGT"));
				pstmt.setString(5, Utility.getPropertyValue("CLAIM_AT_OTHER_RET"));
				pstmt.setString(6, Utility.getPropertyValue("CLAIM_AT_OTHER_AGT"));
				pstmt.setString(7, Utility.getPropertyValue("CLAIM_AT_BO"));
				int rowUpdated = pstmt.executeUpdate();
				logger.info("Total Rows = "+rowUpdated+", Data Inserted Into st_lms_ret_sold_claim_criteria Query - "+pstmt);

				pstmt.close();

				// insert in cl_xcl_update history
				Statement stmt2=con.createStatement();
				String updtCreditXtendedLimitHistory="insert into st_lms_cl_xcl_update_history(organization_id,done_by_user_id,date_time,type,amount,updated_value,total_bal_before_update) values("+ genOrgId+","+userInfoBean.getUserId()+",'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"','CL',"+orgUserData.getCreditLimit()+","+orgUserData.getCreditLimit()+","+"0.00)";
				logger.info("inset Query for st_lms_cl_xcl_update history:"+updtCreditXtendedLimitHistory);
				stmt2.executeUpdate(updtCreditXtendedLimitHistory);
							
				/*
				 * // insert into st_rg_org_daily_tx String
				 * insOrgRgdailyTxQuery="insert into
				 * st_lms_rg_org_daily_tx(organization_id,date) values(?,?)";
				 * pstmt = con.prepareStatement(insOrgRgdailyTxQuery);
				 * pstmt.setInt(1, genOrgId); pstmt.setDate(2, new
				 * java.sql.Date(new Date().getTime())); int
				 * insOrgRgdailytansRows = pstmt.executeUpdate();
				 * logger.info("total rows = "+insOrgRgdailytansRows+"
				 * data inserted into st_rg_org_daily_tx query : "+pstmt);
				 * 
				 * pstmt.close(); // insert into st_rg_org_weekly_tx String
				 * insOrgRgWeeklyTxQuery="insert into
				 * st_lms_rg_org_weekly_tx(organization_id,date) values(?,?)";
				 * pstmt = con.prepareStatement(insOrgRgWeeklyTxQuery);
				 * pstmt.setInt(1, genOrgId); Calendar cal
				 * =Calendar.getInstance(); cal.set(Calendar.DAY_OF_WEEK, 2);
				 * Date weekMondayDate = cal.getTime(); pstmt.setDate(2,new
				 * java.sql.Date(weekMondayDate.getTime())); int
				 * insOrgRgWeeklytansRows = pstmt.executeUpdate();
				 * logger.info("total rows = "+insOrgRgWeeklytansRows+"
				 * data inserted into st_rg_org_weekly_tx query : "+pstmt);
				 * pstmt.close();
				 */
				// generate the auto password
				String autoPass = null;

				if ("integer".equalsIgnoreCase(((String) LMSUtility.sc.getAttribute("RETAILER_PASS"))
						.trim())) {
					autoPass = AutoGenerate.autoPasswordInt() + "";
				} else {
					autoPass = AutoGenerate.autoPassword();
				}
				logger.info("Password Generated: " + autoPass);
				// insert data into st_lms_user_master table
				String userReg = QueryManager.insertST3AgentDetails();
				pstmt = con.prepareStatement(userReg);
				pstmt.setInt(1, genOrgId);	
				pstmt.setInt(2, roleId);
				pstmt.setString(3, orgUserData.getOrgType());
				pstmt.setString(4, "1");
				pstmt.setString(5, orgUserData.getUserName().trim()
						.toLowerCase());
				pstmt.setString(6, MD5Encoder.encode(autoPass));
				pstmt.setString(7, orgUserData.getStatus());
				pstmt.setString(8, orgUserData.getSecQues());
				pstmt.setString(9, orgUserData.getSecAns());
				pstmt.setTimestamp(10, new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt.setString(11, "Y");
				pstmt.setInt(12, userInfoBean.getUserId());
				pstmt.setInt(13, orgUserData.getRegisterById());
				if(orgUserData.getTpUserId() != null){
					pstmt.setString(14, orgUserData.getTpUserId());
				}else{
					pstmt.setString(14, null);
				}
				pstmt.executeUpdate();
				logger.info("user type::"+userInfoBean.getUserType());
				logger.info("bo user created query : " + pstmt);

				// get the generated user_id
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					int userId = rs.getInt(1);
					logger.info("bo user created and user id : "
							+ userId);

					// insert data into st_lms_user_contact_details
					String insertContactDetail = QueryManager
							.insertST3ContactsDetails();
					pstmt = con.prepareStatement(insertContactDetail);
					pstmt.setInt(1, userId);
					pstmt.setString(2, orgUserData.getFirstName());
					pstmt.setString(3, orgUserData.getLastName());
					pstmt.setString(4, orgUserData.getEmail());
					pstmt.setLong(5, orgUserData.getPhone());
					pstmt.setString(6, orgUserData.getIdType());
					pstmt.setString(7, orgUserData.getIdNo());
					pstmt.setLong(8, orgUserData.getMobile());
					int contactDetailRows = pstmt.executeUpdate();
					logger.info("total rows update in st_lms_user_contact_details table == "
									+ contactDetailRows + " pstmt : " + pstmt);

					// insert password details into st_password_history table
					String passwordDetails = QueryManager
							.insertST3PasswordDetails();
					pstmt = con.prepareStatement(passwordDetails);
					pstmt.setInt(1, userId);
					pstmt.setString(2, MD5Encoder.encode(autoPass));
					int passHistoryRows = pstmt.executeUpdate();
					logger.info("total rows update in passHistory table == "
									+ passHistoryRows);
					pstmt.close();

					//	Insert Data In st_lms_user_ip_time_mapping Table.
					LoginTimeIPValidationHelper loginTimeIPValidationHelper = new LoginTimeIPValidationHelper();
					loginTimeIPValidationHelper.insertUserTimeLimitData(userId, con);

					// insert organization history into
					// st_lms_organization_master_history table
					//UserInfoBean loginUserBean = (UserInfoBean)ServletActionContext.getRequest().getSession().getAttribute("USER_INFO");
					/*String insertOrgHistory = QueryManager
							.insertST3OrganizationHistory()
							+ " values( "
							+ userInfoBean.getUserId()
							+ ", "
							+ genOrgId
							+ ", '"
							+ orgUserData.getAddrLine1()
							+ "', '"
							+ orgUserData.getAddrLine2()
							+ "', '"
							+ orgUserData.getDivision()
							+ "', '"
							+ orgUserData.getArea()
							+ "', '"
							+ orgUserData.getCity()
							+ "', "
							+ orgUserData.getPin()
							+ ", "
							+ orgUserData.getCreditLimit()
							+ ", "
							+ orgUserData.getSecurity()
							+ ",'ACTIVE_MANUAL_"+loginType+"'"
							+ ",'New Retailer Organization created','"
							+ orgUserData.getStatusorg()
							+ "',' "
							+ new java.sql.Timestamp(new Date().getTime()).toString()
							+ "','"
							+orgUserData.getAutoScrap()
							+ "','"
							+ orgUserData.getReconReportType()
							+ "'"
							+ ")";
					stmt = con.prepareStatement(insertOrgHistory);
					int historyRows = stmt.executeUpdate();
					logger.info(historyRows
							+ " rows updated and Query to update history :: "
							+ insertOrgHistory);*/

					// insert into service role mapping for organization
					String serRole = "insert into st_lms_service_role_mapping (role_id,organization_id,id,status) values ";
					int id[] = orgUserData.getId();
					String status[] = orgUserData.getStatusTable();
					StringBuilder values = new StringBuilder();
					for (int i = 0; i < id.length; i++) {
						values.append("(" + roleId + "," + genOrgId + ","
								+ id[i] + ",'" + (status[i].split("-"))[0]
								+ "'),");
					}
					serRole = serRole
							+ values.substring(0, values.length() - 1);
					logger.info("Service Role entry-----" + serRole);
					pstmt = con.prepareStatement(serRole);
					pstmt.executeUpdate();

					// insert into st_user_priv_master
					/*
					 * String insertUserPrivQuery="insert into user_priv_master
					 * select "+userId+","+roleId+", aa.pid, bb.status from
					 * role_privl_mapping aa, priviledge_rep bb where aa.pid =
					 * bb.pid and aa.role_id = "+roleId; pstmt =
					 * con.prepareStatement(insertUserPrivQuery); int
					 * userPrivMasterRows = pstmt.executeUpdate();
					 * logger.info("total rows = "+userPrivMasterRows+"
					 * data inserted into st_user_priv_master query : "+pstmt);
					 */
					
					// Insertion into st_dg_last_sold_ticket
					PreparedStatement addRetOrgQry = con.prepareStatement("insert into st_dg_last_sold_ticket(ret_org_id,terminal_ticket_number,web_ticket_number,terminal_ticket_status,web_ticket_status) values(?,0,0,null,null)");
					addRetOrgQry.setInt(1, genOrgId);
					logger.info("addRetOrgQry:" + addRetOrgQry);
					
					addRetOrgQry.executeUpdate();
					
					//--- Insertion of Organization Id in CS's 'Last Sale Txn Table'
					PreparedStatement lastTxnPstmt = con.prepareStatement("insert into st_lms_last_sale_transaction values(?,0,0,0)");
					lastTxnPstmt.setInt(1, genOrgId);
					logger.info("lastTxnQuery:" + lastTxnPstmt);
					
					lastTxnPstmt.executeUpdate();
					//---
					
					String insertUserPriv = null;
					String privStatus = null;
					String tableName = null;
					for (int i = 0; i < id.length; i++) {
						privStatus = status[i].split("-")[0];
						tableName = status[i].split("-")[1];
						if (privStatus.equals("ACTIVE")) {
							privStatus = "rpm.status";
						} else {
							privStatus = "'INACTIVE'";
						}
						insertUserPriv = "insert into st_lms_user_priv_mapping (priv_id ,user_id , role_id, service_mapping_id, status) select "
								+ "  distinct(rpm.priv_id),"
								+ userId
								+ ","
								+ roleId
								+ ",rpm.service_mapping_id, "
								+ privStatus
								+ " from st_lms_role_priv_mapping rpm, "
								+ tableName
								+ " pr  where rpm.role_id = "
								+ roleId
								+ " and rpm.priv_id = pr.priv_id and service_mapping_id="
								+ id[i] + "";
						pstmt = con.prepareStatement(insertUserPriv);
						pstmt.executeUpdate();
					}

					// send mail to user is commented now because retailer is
					// not online
					if (orgUserData.getOrgType().equals("AGENT")) {
						// assign privilege to RETAILER
						assignEmailPriviledges(userId,
								orgUserData.getOrgType(), orgUserData
										.getEmail(), orgUserData
										.getEmailPrivId(), con);

					}

					// added for offline sale
					String isRetailerOffline = (String) LMSUtility.sc.getAttribute("RET_OFFLINE");
					String isLoginBind = (String) LMSUtility.sc.getAttribute("LOGIN_BINDING");
					boolean  isSimBind ="YES".equalsIgnoreCase((String) LMSUtility.sc.getAttribute("sim_binding"))?true:false;
					int retOfflineId = 0;
					StringBuilder sb = new StringBuilder();
					String address = null;
					String latLon = null;
					//if("YES".equalsIgnoreCase(isLoginBind.trim())){
						if (!"YES".equalsIgnoreCase(isRetailerOffline.trim())) {
							orgUserData.setIsOffLine("NO");
						}
						DrawGameOfflineHelper helper = new DrawGameOfflineHelper();
						if(orgUserData.getModelName()==null){
							orgUserData.setModelName("-1");
							orgUserData.setTerminalId("-1");
						}
						address = sb.append(orgUserData.getAddrLine1()).append(",").append(orgUserData.getAddrLine2()).append(" ").append(orgUserData.getCity()).append(" ").append(orgUserData.getCountry()).toString();
						latLon = CommonFunctionsHelper.getLongitudeLatitude(address);
						retOfflineId = helper.saveOfflineRetData(userId,genOrgId, orgUserData.getIsOffLine(),orgUserData.getModelName(),orgUserData.getTerminalId(), con,latLon,orgUserData.getCity(),isLoginBind,orgUserData.getSim(),orgUserData.getSimModelName(),isSimBind);
						if (retOfflineId < 1) {
							throw new LMSException(textProvider.getText("error.usr.not.create"));
						}
					//}
					// rg calling
					//ResponsibleGaming.rgInsertion(userId, genOrgId);
					ResponsibleGaming.rgInsertion(userId, genOrgId ,con);
                    
					//here insert the entry into the st_ola_org_affiliate_mapping tabla\le
					if((LMSFilterDispatcher.getIsOLA()).equalsIgnoreCase("YES"))
					{
					pstmt = con.prepareStatement("insert into st_ola_org_affiliate_mapping(organization_id, ref_user_id) values(?,?)");
					pstmt.setInt(1, genOrgId);
					pstmt.setString(2, orgUserData.getUserName());
					pstmt.executeUpdate();
					//con.commit();
					}
					
					int noOfExpDays = Integer.parseInt(com.skilrock.lms.common.Utility.getPropertyValue("USER_MAPPING_ID_EXPIRY").trim());
					String thirdPartyAddress = com.skilrock.lms.common.Utility.getPropertyValue("ADD_OF_THIRD_PARTY_GEN_MAPPING_ID").trim();
					
					String systemUserName = com.skilrock.lms.common.Utility.getPropertyValue("SYSTEM_AUTHENTICATION_USERNAME").trim();
					String systemUserPassword = com.skilrock.lms.common.Utility.getPropertyValue("SYSTEM_AUTHENTICATION_PASSWORD").trim();
					
					boolean isGenPlaceLMS = "true".equalsIgnoreCase(com.skilrock.lms.common.Utility.getPropertyValue("MAPPING_ID_GEN_BY_THIRD_PARTY").trim());
					
					UserIdMappingBean userIdMappingBean = new UserIdMappingBean();
					LmsUserIdMappingRequestBean lmsUserIdMappingRequestBean = null;
					if (isGenPlaceLMS) {
						LMSServicesClient client = new LMSServicesClient(thirdPartyAddress);
						LMSServicesPortType service = client.getLMSServicesHttpPort();
						lmsUserIdMappingRequestBean = new LmsUserIdMappingRequestBean();
						lmsUserIdMappingRequestBean.setSystemUserName(systemUserName);
						lmsUserIdMappingRequestBean.setSystemUserPassword(systemUserPassword);
						lmsUserIdMappingRequestBean.setAll(false);
						lmsUserIdMappingRequestBean.setSpecific(true);
						lmsUserIdMappingRequestBean.setUserId(userId);
						lmsUserIdMappingRequestBean.setActivity("RET_REG");
						lmsUserIdMappingRequestBean.setExpiryDays(noOfExpDays); 
						lmsUserIdMappingRequestBean.setDoneByUserId(userInfoBean.getUserOrgId());
						logger.debug("Requesting Third Party For Random ID from "+ orgUserData.getUserName() + "With user Id " + userId);
						LmsUserIdMappingResponseBean lmsUserIdMappingResponseBean = service.getRandomMappingIdFromThirdParty(lmsUserIdMappingRequestBean);
						
						if(!lmsUserIdMappingResponseBean.isSuccess()){
							logger.error("Error from Third Party... throwing exception ");
							throw new LMSException(); // TEMP
						}
			
						LmsUserIdMappingRequestBean responseBean = lmsUserIdMappingResponseBean.getLmsUserIdMappingRequestBean();
						logger.error("SUCCESS from Third Party... ");
						logger.error("SUCCESS from Third Party... RANDOM ID FOR " +responseBean.getUserId());
						userIdMappingBean.setThirdPartyGeneration(true);
						userIdMappingBean.setNewGenerationDateTime(responseBean.getNewGenerationDateTime());
						userIdMappingBean.setNewCodeExpiryDateTime(responseBean.getNewCodeExpiryDateTime());
						userIdMappingBean.setAdvGenerationDateTime(responseBean.getAdvGenerationDateTime());
						userIdMappingBean.setAdvCodeExpiryDateTime(responseBean.getAdvCodeExpiryDateTime());
						userIdMappingBean.setUserMappingIdMap(CommonMethods.prepareHashMapFromAnyTypeMap(responseBean.getUserMappingIdMap()));
						userIdMappingBean.setAdvUserMappingIdMap(CommonMethods.prepareHashMapFromAnyTypeMap(responseBean.getAdvUserMappingIdMap()));
					}else{
						userIdMappingBean.setThirdPartyGeneration(false);
					}
						userIdMappingBean.setAll(false);
						userIdMappingBean.setSpecific(true);
						userIdMappingBean.setUserId(userId);
						userIdMappingBean.setActivity("RET_REG");
						userIdMappingBean.setExpiryDays(noOfExpDays); 
						userIdMappingBean.setDoneByUserId(userInfoBean.getUserOrgId());
						CommonMethods.getUserIdToGenMappindId(userIdMappingBean, con);

						//	Insert Registration Messages
						MessageInboxDaoImpl.getSingleInstance().addRegistrationMessage(userId, "RETAILER", "WEB", con);
						MessageInboxDaoImpl.getSingleInstance().addRegistrationMessage(userId, "RETAILER", "TERMINAL", con);

						if("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE())){
							ServerStartUpData.updateOrgDataForSLE("RETAILER" , genOrgId, con);
						}

						con.commit();

						if(ServicesBean.isSLE()) {
							UserDataBean dataBean = UserMgmtController.getInstance().getUserInfo(orgUserData.getUserName().trim());
							dataBean.setCreatorUserId(userInfoBean.getUserId());
							NotifySLE notifySLE = new NotifySLE(SLE.Activity.USER_REGISTER, dataBean);
							notifySLE.start();
						}
						
					if (ServicesBean.isVS()) {
						VSRequestBean vsRequestBean = new VSRequestBean(orgUserData.getOrgName(), genOrgId, orgUserData.getUserName(), userId, orgUserData.getUserName());
						VirtualSportsControllerImpl.Single.INSTANCE.getInstance().registerRetailer(vsRequestBean);
					}

						logger.info("retailer registration completed successfully");
					//now update the comm variance for the new organization
					//add one more entry in the org game data map
					//This is harcoded check for OLA to run independently.
					if (ServicesBean.isDG()) //
						Util.orgDataMap.put(genOrgId, ServerStartUpData.getOrgGameData(genOrgId, con, "RETAILER"));
					if (ServicesBean.isIW())
						Util.iwOrgDataMap.put(genOrgId, ServerStartUpData.getOrgGameDataIW(genOrgId, con, "RETAILER"));
					if (ServicesBean.isVS())
						Util.vsOrgDataMap.put(genOrgId, ServerStartUpData.getOrgGameDataVS(genOrgId, con, "RETAILER"));
					//logger.info("sale variance after registration: " + Util.getSaleCommVariance(genOrgId,2));
					// send mail to user is commented now because retailer is
					// not online
					//String isRetaileOnline = (String) LMSUtility.sc.getAttribute("RET_ONLINE");
					String isRetaileOnline = orgUserData.getIsRetailerOnline();
					errorMap.put("NewPassword", autoPass);
					if ("YES".equalsIgnoreCase(isRetaileOnline.trim())) {
						String msgFor = textProvider.getText("error.thank.for.reg.game.sys.login.det.are");
						String emailMsgTxt = "<html><table><tr><td>Hi "
								+ orgUserData.getFirstName()
								+ " "
								+ orgUserData.getLastName()
								+ "</td></tr><tr><td>"
								+ msgFor
								+ "</td></tr></table><table><tr><td>User Name :: </td><td>"
								+ orgUserData.getUserName().trim()
								+ "</td></tr><tr><td>password :: </td><td>"
								+ autoPass.toString()
								+ "</td></tr><tr><td>log on </td><td>"
								+ LMSFilterDispatcher.webLink + "/"
								+ LMSFilterDispatcher.mailProjName
								+ "/</td></tr></table></html>";
						MailSend mailSend = new MailSend(
								orgUserData.getEmail(), emailMsgTxt);
						mailSend.setDaemon(true);
						mailSend.start();
						// MailSend.sendMailToUser(email, autoPass,
						// userName.trim(), firstName, lastName, msgFor);
						logger.info("mail sent after commit");
					}
					return errorMap;
				} else {
					logger.info("User is not created ");
					throw new LMSException(textProvider.getText("error.usr.not.create"));
				}
			} else {
				logger.info("organization is not created ");
				throw new LMSException(textProvider.getText("error.org.not.create"));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("sql exception", se);
		}catch (Exception se) {
			se.printStackTrace();
			throw new LMSException("exception", se);
		} finally {
			DBConnect.closeCon(con);

			try {
					if(con!=null){
						con.close();
					}
					if(rs!=null){
						rs.close();
					}
					if(pstmt!=null){
						pstmt.close();
					}
					if(stmt!=null){
						stmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			//	Licensing Server Validation
			LSControllerImpl.getInstance().clientValidation();
		}
	}

	// Added by Umesh

	public Map<Integer, String> getMailingReportTitle(String userType) {
		Map<Integer, String> mailReportTitle = new TreeMap<Integer, String>();
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();
			String mailReportTitleQuery = "select email_pid, priv_title from st_lms_report_email_priviledge_rep where priv_owner = '"
					+ userType + "' and status ='ACTIVE'";
			ResultSet rs = stmt.executeQuery(mailReportTitleQuery);
			while (rs.next()) {
				String privTitle = rs.getString("priv_title");
				int emailPid = rs.getInt("email_pid");
				mailReportTitle.put(emailPid, privTitle);
			}
			rs.close();
			stmt.close();
		} catch (SQLException se) {
			se.printStackTrace();
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

		return mailReportTitle;
	}

	public static Map<String, String> verifyOrgName(String orgName, String userName,
			Connection conn) throws LMSException {
		Connection con = conn;
		Map<String, String> errorMap = new TreeMap<String, String>();
		try {
			Statement stmt = con.createStatement();
			String organizationName = QueryManager.getST3OrgName()
					+ " where name='" + orgName.trim() + "'";
            logger.info("VERIFY Organization Name::"+organizationName);
			ResultSet res = stmt.executeQuery(organizationName);
			if ("admin".equalsIgnoreCase(orgName) || res.next()) {
				logger.info("Organization Name Already exits !!");
				errorMap.put("orgError", textProvider.getText("error.org.name.already.exist")+" !!");
				errorMap.put("returnTypeError", "input");
			} else {
				errorMap.put("orgError", "A");
			}

			// check is 'user_name' already exists in st_lms_user_master table
			String getUsersName = QueryManager.getST3UserName()
					+ "where user_name= '" + userName.trim() + "'";
			res = stmt.executeQuery(getUsersName);
			logger.info("CHECK USER NAME::"+getUsersName);
			if ("admin".equalsIgnoreCase(userName) || res.next()) {
				logger.info("user already exists !!");
				errorMap.put("userError", textProvider.getText("error.uname.already.exist")+" !!");
				errorMap.put("returnTypeError", "input");
			} else {
				errorMap.put("userError", "A");
			}
			return errorMap;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(se);
		}
	}
/**
 * Added to set userInfoBean data for organization_type
 * @param organization_type
 * @param userInfoBean
 * @return
 */
	public UserInfoBean changeUserBean(String organization_type,UserInfoBean userInfoBean) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
	try {
			logger.info("changing user bean for admin to "+organization_type);
			selectQuery = "select  user_id,organization_id from st_lms_user_master um where organization_type=? and isrolehead='Y' limit 1";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1,organization_type);
			rs = pstmt.executeQuery();
			logger.info("changing query"+pstmt);
			while (rs.next()) {
				userInfoBean.setUserId(rs.getInt("user_id"));
				userInfoBean.setUserOrgId(rs.getInt("organization_id"));
				userInfoBean.setUserType(organization_type);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return userInfoBean;
	}
	
	private static String generateAccessCodeForOrg(String stateCode,int newOrgNumber,String userName,String orgType,Connection con,String countryDep) throws LMSException{
			PreparedStatement pstmt =null;
			PreparedStatement pstmt1 =null;
			ResultSet rs =null;
		try{

			String generatedCode ="";
			logger.info("Inside Code Generation stateCode " + stateCode+" userName"+userName+"orgType"+orgType+"countryDep "+countryDep+"newOrgNumber "+newOrgNumber);		
			if("ZIMBABWE".equalsIgnoreCase(countryDep)||"NIGERIA".equalsIgnoreCase(countryDep)){
				
				if("AGENT".equalsIgnoreCase(orgType)){
					generatedCode = userName;
					if(userName.matches(".*[0-9].*")){
						throw new LMSException(textProvider.getText("error.inv.agt.uname"));
					}
					pstmt=con.prepareStatement("update st_lms_state_master set no_agt_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
					int update =pstmt.executeUpdate();
					if(update==0){
						logger.info("no_agt_registered not updated:" + pstmt);
						throw new LMSException(textProvider.getText("error.no.agt.reg.not.update"));
					}
				}else if("RETAILER".equalsIgnoreCase(orgType)){
					pstmt1 = con.prepareStatement("select count(organization_id) nofRet from st_lms_organization_master where parent_id=(select organization_id from st_lms_user_master where user_name='"+userName+"')");	
					rs = pstmt1.executeQuery();
					int retCounter = 0;
					if(rs.next()){
						retCounter=rs.getInt("nofRet");
					}
					
					generatedCode =userName+(retCounter+1);
					pstmt=con.prepareStatement("update st_lms_state_master set no_ret_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
					int update =pstmt.executeUpdate();
					if(update ==0){
						logger.info("no_ret_registered not updated:" + pstmt);
						throw new LMSException(textProvider.getText("error.no.ret.reg.not.update"));
						
					}
					
				}else {
					logger.info("Invalid Org Type " + orgType);
					throw new LMSException(textProvider.getText("error.org.type.not.pass.proper.during.access.code.create"));
					
				}
				

				
			}
			else if("GHANA".equalsIgnoreCase(countryDep)||"BENIN".equalsIgnoreCase(countryDep) || "SAFARIBET".equalsIgnoreCase(countryDep)){
					generatedCode =newOrgNumber+"";
				if("AGENT".equalsIgnoreCase(orgType)){
				    //4 digit in case of agt
					
					if(newOrgNumber>9999){
						logger.info("Agent Registration Limit Reached" + newOrgNumber);
						throw new LMSException(textProvider.getText("error.agt.reg.limit.reach"));
					}
					while(generatedCode.length()< 4){
								generatedCode = "0" + generatedCode; 
								}
							
						generatedCode = stateCode + generatedCode;
						pstmt=con.prepareStatement("update st_lms_state_master set no_agt_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
						int update =pstmt.executeUpdate();
						if(update==0){
							logger.info("no_agt_registered not updated:" + pstmt);
							throw new LMSException(textProvider.getText("error.no.agt.reg.not.update"));
						}
					//  stmt.executeUpdate("update st_lms_state_master set no_agt_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
					 			 
					}
					else if("RETAILER".equalsIgnoreCase(orgType)){
						if(newOrgNumber>99999){
							logger.info("Retailer Registration Limit Reached" + newOrgNumber);
							throw new LMSException(textProvider.getText("error.ret.reg.limit.reach"));
						}
						//5 digit in case of retailer
						while(generatedCode.length()<5){
							generatedCode = "0" + generatedCode; 
						}
						generatedCode = stateCode + generatedCode;
						pstmt=con.prepareStatement("update st_lms_state_master set no_ret_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
						int update =pstmt.executeUpdate();
						if(update ==0){
							logger.info("no_ret_registered not updated:" + pstmt);
							throw new LMSException(textProvider.getText("error.no.ret.reg.not.update"));
							
						}
						//stmt.executeUpdate("update st_lms_state_master set no_ret_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
					}			
					else {
						
						logger.info("Invalid Org Type " + orgType);
						throw new LMSException(textProvider.getText("error.org.type.not.pass.proper.during.access.code.create"));
					}
						
			}else if("SIKKIM".equalsIgnoreCase(countryDep)||"PHILIP".equalsIgnoreCase(countryDep) || "INDIA".equalsIgnoreCase(countryDep)){
					generatedCode =newOrgNumber+"";
				if("AGENT".equalsIgnoreCase(orgType)){
				    //4 digit in case of agt
					
					if(newOrgNumber>9999){
						logger.info("Agent Registration Limit Reached" + newOrgNumber);
						throw new LMSException(textProvider.getText("error.agt.reg.limit.reach"));
					}
					while(generatedCode.length()< 4){
								generatedCode = "0" + generatedCode; 
								}
							
						generatedCode = stateCode + generatedCode;
						pstmt=con.prepareStatement("update st_lms_state_master set no_agt_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
						int update =pstmt.executeUpdate();
						if(update==0){
							logger.info("no_agt_registered not updated:" + pstmt);
							throw new LMSException(textProvider.getText("error.no.agt.reg.not.update"));
						}
					//  stmt.executeUpdate("update st_lms_state_master set no_agt_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
					 			 
					}
					else if("RETAILER".equalsIgnoreCase(orgType)){
						if(newOrgNumber>99999){
							logger.info("Retailer Registration Limit Reached" + newOrgNumber);
							throw new LMSException(textProvider.getText("error.ret.reg.limit.reach"));
						}
						//5 digit in case of retailer
						while(generatedCode.length()<5){
							generatedCode = "0" + generatedCode; 
						}
						generatedCode = stateCode + generatedCode;
						pstmt=con.prepareStatement("update st_lms_state_master set no_ret_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
						int update =pstmt.executeUpdate();
						if(update ==0){
							logger.info("no_ret_registered not updated:" + pstmt);
							throw new LMSException(textProvider.getText("error.no.ret.reg.not.update"));
							
						}
						//stmt.executeUpdate("update st_lms_state_master set no_ret_registered="+newOrgNumber+ " where state_code='"+stateCode+"'");
					}			
					else {
						
						logger.info("Invalid Org Type " + orgType);
						throw new LMSException(textProvider.getText("error.org.type.not.pass.proper.during.access.code.create"));
					}
						
			} else {
				logger.info("Invalid Country Deployed  " + countryDep);
				throw new LMSException(textProvider.getText("error.inv.country.not.auth.create.org"));
				
			}
		
			logger.info("Generated Access Code is : " + generatedCode);
			
			return generatedCode;
			
		}catch(Exception e){
			e.printStackTrace();
			throw new LMSException(textProvider.getText("error.org.type.not.pass.proper"));
		}finally{
			
			try{
				if(pstmt!=null){
					pstmt.close();
					
				}
				if(rs!=null){
					rs.close();
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
	private static void insertIntoAgentRoleMapping(int AgentId,int roleId,Connection con){
		PreparedStatement pstmt = null;
		try {
			String userIpMapping = "";
			if (roleId == 1) {
				 userIpMapping = "INSERT INTO st_lms_role_agent_mapping(role_id,agent_id) values(?,?);";
			}else{
				 userIpMapping = "INSERT INTO st_lms_role_agent_mapping(role_id,agent_id) values(?,?),(1,?);";
			}
			pstmt = con.prepareStatement(userIpMapping);
			pstmt.setInt(1, roleId);
			pstmt.setInt(2, AgentId);
			if (roleId != 1) {
				pstmt.setInt(3, AgentId);
			}
			logger.info("inside insertIntoAgentRoleMapping pstmt {} "+ pstmt);
			pstmt.executeUpdate();
		} catch (SQLException se) {
			logger.error("sql exception", se);
		} catch (Exception e) {
			logger.error("Some Error while mapping into role agent ", e);
		}finally {
			DBConnect.closeResource(pstmt);
		}
	}
}
