package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.HistoryBean;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.web.drawGames.common.Util;
/**
 * This class provides methods to get organization details and to edit
 * organization details
 * 
 * @author Skilrock Technologies
 * 
 */
public class AgentManagementHelper {
	
	static Log logger = LogFactory.getLog(AgentManagementHelper.class);
	public static void editEextendedCreditLimit() {
		Connection con = null;

		try {

			 
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt4 = con.createStatement();
			stmt4
					.executeUpdate("update st_lms_organization_master set extended_credit_limit=0.0 where  extends_credit_limit_upto='"
							+ new java.sql.Timestamp(new Date().getTime())
							+ "'");
			con.commit();
		} catch (SQLException exception) {

		}
	}

	private int calculateExtendsCreditLimitUpto(java.sql.Date date) {
		if (date == null) {
			return 0;
		}
		long days = 0;

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
		}
		// System.out.println(" dd days : "+days +" hours = "+hours);
		// System.out.println(date +", extendedDate = "+extendedDate.getTime()
		// +" ,today : "+today.getTime());

		return (int) days;
	}

	public void editOrgCreditLimitDetails(OrganizationBean orgBean,double amount,UserInfoBean userbean,Connection con) throws LMSException {

		PreparedStatement insertHistoryPstmt=null;
		
		try {
			
			String  transactionType=amount>0?"CREDIT":"DEBIT";
			
			OrgCreditUpdation.updateOrganizationBalWithValidate(Math.abs(amount), "CL", transactionType, orgBean.getOrgId(), orgBean.getParentOrgId(), orgBean.getOrgType(), 0, con);
			
	
         // insert in cl_xcl_update history
			insertHistoryPstmt=con.prepareStatement("insert into st_lms_cl_xcl_update_history(organization_id,done_by_user_id,date_time,type,amount,updated_value,total_bal_before_update) values(?,?,?,?,?,?,?)");
			insertHistoryPstmt.setInt(1, orgBean.getOrgId());
			insertHistoryPstmt.setInt(2, userbean.getUserId());
			insertHistoryPstmt.setTimestamp(3, Util.getCurrentTimeStamp());
			insertHistoryPstmt.setString(4, "CL");
			insertHistoryPstmt.setDouble(5, amount);
			insertHistoryPstmt.setDouble(6, orgBean.getAvailableCredit()-orgBean.getClaimableBal()+amount);
			insertHistoryPstmt.setDouble(7, orgBean.getAvailableCredit()-orgBean.getClaimableBal());
			insertHistoryPstmt.executeUpdate();
			
		} catch (SQLException e) {

			e.printStackTrace();

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
			}

		} 

	}
	
	
	/*public double editOrgCreditLimitDetails(int orgid, double creditLimit,
			double securityDeposit, double currentCreditAmt,
			double exCreditAmt, String userType, int loggedInOrgId,
			int loggedinUserId) throws LMSException {

		int orgId = orgid;
		creditLimit = CommonMethods.fmtToTwoDecimal(creditLimit);
		Connection con = null;
		double availableCredit = 0.0;
		try {
			 
			Statement stmt1 = null;
			Statement stmt2 = null;
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			stmt1 = con.createStatement();
			stmt2 = con.createStatement();
			System.out.println("org id here  " + orgId);
			String orgStatus = null;
			String reasonToUpd = "ACTIVE_MANUAL_BO";
			String comment = "credit "+creditLimit;
			System.out.println("reason= "+reasonToUpd+" comment= "+comment);
			availableCredit = CalculateAvailable.calculateAvlCredit(
					creditLimit, exCreditAmt, currentCreditAmt);
			// System.out.println("1111111 " + availableCredit);
			availableCredit = CommonMethods.fmtToTwoDecimal(availableCredit);
			// System.out.println("22222222 " + availableCredit);

			EmailBean emailBean = null;
			Map<String, Object> map = OrgCreditUpdation
					.fetchOrgStatusWithMailMsg(orgId, con, availableCredit);
			if (map != null && map.size() == 2) {
				orgStatus = (String) map.get("ORG_STATUS");
				emailBean = (EmailBean) map.get("EMAIL_BEAN");
				
			} else { // if fetchOrgStatusWithMailMsg would'nt work
				if (availableCredit > 0) {
					orgStatus = "ACTIVE";
					reasonToUpd = "ACTIVE_AUTO_ACT";
					comment = "credit "+creditLimit;
				} else {
					orgStatus = "INACTIVE";
					reasonToUpd = "INACTIVE_AUTO_ACT";
					comment = "credit "+creditLimit;
				}
			}
         // insert in cl_xcl_update history
			
			String updtCreditXtendedLimitHistory="insert into st_lms_cl_xcl_update_history(organization_id,date_time,type,amount,updated_value,total_bal_before_update) select organization_id,'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"','CL',"+availableCredit+"-available_credit,"+availableCredit+"-claimable_bal,available_credit-claimable_bal from st_lms_organization_master where organization_id="+orgId;
			System.out.println("inset Query for st_lms_cl_xcl_update history:"+updtCreditXtendedLimitHistory);
			stmt2.executeUpdate(updtCreditXtendedLimitHistory);
			
			
			String updateQuery = "update st_lms_organization_master set credit_limit='"
					+ creditLimit
					+ "',available_credit="
					+ availableCredit
					+ ", security_deposit = "
					+ securityDeposit
					+ ",organization_status='"
					+ orgStatus
					+ "' where organization_id=" + orgId + "";
			System.out.println("**updateQuery**" + updateQuery);
			stmt1.executeUpdate(updateQuery);
			String updateHistoryQuery = null;

			updateHistoryQuery = "insert into st_lms_organization_master_history select '"+ loggedinUserId +"', organization_id, addr_line1, addr_line2, city, pin_code,security_deposit,credit_limit, '"+ reasonToUpd +"','"+ comment +"','"+ orgStatus +"','"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"',pwt_scrap, recon_report_type from st_lms_organization_master where organization_id = "+orgId;
			System.out.println("**updateHistoryQuery**" + updateHistoryQuery);
			stmt2.executeUpdate(updateHistoryQuery);
			
			reasonToUpd = "CREDIT_CHANGED_BO";
			comment = "Credit "+creditLimit;
			
			
				
			
				.addBatch("insert into st_lms_organization_master_history select "
						+ userbean.getUserId()
						+ ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'CREDIT_CHANGED_"+ userbean.getRoleName().split(" ")[0] +"','"+ msg +"', organization_status,'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
						+ retOrgId[i]);
				System.out.println("insert into st_lms_organization_master_history select " + userbean.getUserId() + ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'CREDIT_CHANGED_"+ userbean.getRoleName().split(" ")[0] +"','"+ msg +"', organization_status,'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id=" + retOrgId[i]);
			
			
			String updtForCreditLimitInHist = "insert into st_lms_organization_master_history select '"+ loggedinUserId +"', organization_id, addr_line1, addr_line2, city, pin_code,security_deposit,credit_limit, '"+ reasonToUpd +"','"+ comment +"','"+ orgStatus +"','"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"',pwt_scrap, recon_report_type from st_lms_organization_master where organization_id = "+orgId;
			System.out.println("insert Query for Organisation Master history"+updtForCreditLimitInHist);
			stmt2.executeUpdate(updtForCreditLimitInHist);
			
			
			con.commit();

			// sending mail if
			if (emailBean != null && emailBean.getIsEmailSendControlFlag()) {
				MailSend mailSend = new MailSend(emailBean.getTo(), emailBean
						.getEmailMsg());
				mailSend.setDaemon(true);
				mailSend.start();
			}

		} catch (SQLException e) {

			e.printStackTrace();

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
			}

		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}

		}

		return availableCredit;
	}*/

	
	
	public double editCreditLimit(double amount, double securityDeposit,
			UserInfoBean userBean, OrganizationBean orgBean, String requestIp)
			throws LMSException {
		Connection con=null;
		PreparedStatement pstmt=null;
		int updatedRow=0;
		
		try{
		con=DBConnect.getConnection();

		HistoryBean historyBean = new HistoryBean(orgBean.getOrgId(), userBean.getUserId(), "", requestIp);

		if(amount-orgBean.getOrgCreditLimit() != 0)
			editOrgCreditLimitDetails(orgBean,amount-orgBean.getOrgCreditLimit(), userBean, con);

		if(securityDeposit-orgBean.getSecurityDeposit() != 0) {
			//OrgCreditUpdation.updateOrgSecurityDeposit(orgBean.getOrgId(), securityDeposit, con);
			historyBean.setChangeType("SECURITY_DEPOSIT");
			historyBean.setChangeValue(String.valueOf(orgBean.getSecurityDeposit()));
			historyBean.setUpdatedValue(String.valueOf(securityDeposit));
			if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
				pstmt=con.prepareStatement("UPDATE st_lms_organization_security_levy_master SET collected_security_deposit=? WHERE organization_id=?");
				pstmt.setDouble(1,securityDeposit);
				pstmt.setInt(2, orgBean.getOrgId());
				logger.info("update Sec Deposit Query"+pstmt);
				updatedRow=pstmt.executeUpdate();
				if(updatedRow==0)
					throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}else{
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
			}
		}

		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			DBConnect.closeConnection(con, pstmt);
		}
		return orgBean.getAvailableCredit()+amount-orgBean.getOrgCreditLimit();
		
	}

	
	public double editExtendedLimit(double amount, int noOfDays,
			UserInfoBean userBean, OrganizationBean orgBean)
			throws LMSException {
		Connection con=null;
		try{
		con=DBConnect.getConnection();
		editOrgDetails( amount,  noOfDays,
				 userBean,  orgBean,con);
		
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				se.printStackTrace();			
				}
		}
		return orgBean.getAvailableCredit()+amount-orgBean.getExtendedCredit();
		
	}

	
	
	
	public void editOrgDetails(double amount,int noOfDays,UserInfoBean userBean,OrganizationBean orgBean,Connection con)
			throws LMSException {
		PreparedStatement insertHistoryPstmt=null;
		
		try {
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(amount-orgBean.getExtendedCredit(), "XCL", "CREDIT", orgBean.getOrgId(), orgBean.getParentOrgId(), orgBean.getOrgType(), noOfDays, con);
			
	
         // insert in cl_xcl_update history
			if(isValid){
			insertHistoryPstmt=con.prepareStatement("insert into st_lms_cl_xcl_update_history(organization_id,done_by_user_id,date_time,type,amount,updated_value,total_bal_before_update) values(?,?,?,?,?,?,?)");
			insertHistoryPstmt.setInt(1, orgBean.getOrgId());
			insertHistoryPstmt.setInt(2, userBean.getUserId());
			insertHistoryPstmt.setTimestamp(3, Util.getCurrentTimeStamp());
			insertHistoryPstmt.setString(4, "XCL");
			insertHistoryPstmt.setDouble(5,amount-orgBean.getExtendedCredit());
			insertHistoryPstmt.setDouble(6, orgBean.getAvailableCredit()-orgBean.getClaimableBal()-orgBean.getExtendedCredit() + amount);
			insertHistoryPstmt.setDouble(7, orgBean.getAvailableCredit()-orgBean.getClaimableBal());
			insertHistoryPstmt.executeUpdate();
			}else{
				throw new LMSException();
			}
		} catch (SQLException e) {

			e.printStackTrace();

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
			}

		} 

	
			
	}
	
	
	
	/**
	 * This method is used to edit organization details
	 * 
	 * @param orgid
	 *            organization Id
	 * @param addr1
	 *            address line 1
	 * @param addr2
	 *            address line 2
	 * @param city
	 *            organization city
	 * @param pin
	 *            city pin
	 * @param orgstatus
	 *            organization status to set
	 * @param limit
	 *            organization limit to set
	 */
	/*public double editOrgDetails(int orgid, double extendedLimit,
			double currentCreditAmt, double creditLimit, int daysAfter,
			String userType, int loggedInOrgId, int loggedinUserId)
			throws LMSException {

		int orgId = orgid;
		extendedLimit = CommonMethods.fmtToTwoDecimal(extendedLimit);
		// System.out.println("after format " + extendedLimit);
		Connection con = null;
		
		double availableCredit = 0.0;
		try {
			
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt1 = null;
			Statement stmt2 = null;
			Statement updHistPstmt = con.createStatement();
			boolean updHist = false;
			stmt1 = con.createStatement();
			stmt2 = con.createStatement();

			HttpSession session = ServletActionContext.getRequest().getSession();
			int parentUserId = ((UserInfoBean)session.getAttribute("USER_INFO")).getUserId();
			
			System.out.println("org id here  " + orgId);
			// String updateOrg=QueryManager.updateST3OrgDetails() + "
			// addr_line1='"+orgAddr1+"', addr_line2='"+orgAddr2+"',
			// city='"+orgCity+"',pin_code='"+orgPin+"',credit_limit='"+orgCreditLimit+"',organization_status='"+statusNew+"'
			// where organization_id='"+orgId+"' " ;

			// String str="update st_lms_organization_master set
			// addr_line1='"+orgAddr1+"', addr_line2='"+orgAddr2+"',
			// city='"+orgCity+"',pin_code='"+orgPin+"',credit_limit='"+orgCreditLimit+"',organization_status='"+statusNew+"'
			// where organization_id='"+orgId+"' ";

			DateFormat dtfmt = new SimpleDateFormat("yyyy/MM/dd");
			Date d = null;

			try {
				d = dtfmt.parse(dtfmt.format(new Date()));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long ms = d.getTime();
			ms = ms + daysAfter * 24 * 60 * 60 * 1000l;
			d.setTime(ms);
			System.out.println("date to extend  " + d);

			// double
			// availableCredit=(creditLimit+extendedLimit)-currentCreditAmt;
			availableCredit = CalculateAvailable.calculateAvlCredit(
					creditLimit, extendedLimit, currentCreditAmt);
			// System.out.println("1111111 " + availableCredit);
			availableCredit = CommonMethods.fmtToTwoDecimal(availableCredit);
			// System.out.println("22222222 " + availableCredit);

			String orgStatus = "";
			EmailBean emailBean = null;
			Map<String, Object> map = OrgCreditUpdation
					.fetchOrgStatusWithMailMsg(orgId, con, availableCredit);
			if (map != null && map.size() == 2) {
				orgStatus = (String) map.get("ORG_STATUS");
				emailBean = (EmailBean) map.get("EMAIL_BEAN");
			} else { // if fetchOrgStatusWithMailMsg would'nt work
				String msg = "";
				if (availableCredit > 0) {
					if(orgStatus.equalsIgnoreCase("INACTIVE")){
						msg = "Organization becomes ACTIVE because Available Credit goes Positive";
						updHistPstmt = con.prepareStatement("insert into st_lms_organization_master_history select '"+ parentUserId +"',organization_id, addr_line1, addr_line2, city, pin_code, security_deposit,'"+ (creditLimit + extendedLimit) +"','ACTIVE_AUTO_ACT', '"+ msg +"',  from st_lms_organization_master where organization_id = "+orgId);
						updHist = true;
					}
					orgStatus = "ACTIVE";
				} else {
					if(orgStatus.equalsIgnoreCase("ACTIVE")){
						msg = "Organization becomes INACTIVE because Available Credit goes Negative";
						updHistPstmt = con.prepareStatement("insert into st_lms_organization_master_history select '"+ parentUserId +"',organization_id, addr_line1, addr_line2, city, pin_code, security_deposit,'"+ (creditLimit + extendedLimit) +"','INACTIVE_AUTO_ACT', '"+ msg +"',  from st_lms_organization_master where organization_id = "+orgId);
						updHist = true;
					}
					orgStatus = "INACTIVE";
				}
			}

			
 // insert in cl_xcl_update history
			
			String updtCreditXtendedLimitHistory="insert into st_lms_cl_xcl_update_history(organization_id,date_time,type,amount,updated_value,total_bal_before_update) select organization_id,'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"','XCL',"+availableCredit+"-available_credit,"+availableCredit+"-claimable_bal,available_credit-claimable_bal from st_lms_organization_master where organization_id="+orgId;
			System.out.println("inset Query for st_lms_cl_xcl_update history:"+updtCreditXtendedLimitHistory);
			stmt2.executeUpdate(updtCreditXtendedLimitHistory);
			
			
			// System.out.println("update st_lms_organization_master set
			// extended_credit_limit="+extendedLimit+",available_credit="+availableCredit+",extends_credit_limit_upto='"+new
			// java.sql.Timestamp(d.getTime())+"',organization_status='"+orgStatus+"'
			// where organization_id="+orgId+"");
			stmt1
					.executeUpdate("update st_lms_organization_master set extended_credit_limit="
							+ extendedLimit
							+ ",available_credit="
							+ availableCredit
							+ ",extends_credit_limit_upto='"
							+ new java.sql.Timestamp(d.getTime())
							+ "',organization_status='"
							+ orgStatus
							+ "' where organization_id=" + orgId + "");
			String updateHistoryQuery = null;
			if (userType.equals("BO")) {
				updateHistoryQuery = "insert into st_lms_bo_extended_limit_history(bo_user_id,agent_org_id,extended_credit_limit,date_changed,extends_credit_limit_upto) values("
						+ loggedinUserId
						+ ","
						+ orgId
						+ ","
						+ extendedLimit
						+ ",'"
						+ new java.sql.Timestamp(new java.util.Date().getTime())
						+ "','" + new java.sql.Timestamp(d.getTime()) + "')";
				stmt2.executeUpdate(updateHistoryQuery);
			} else if (userType.equals("AGENT")) {

				updateHistoryQuery = "insert into st_lms_agent_extended_limit_history(agent_org_id,agent_user_id,retailer_org_id,extended_credit_limit,date_changed,extends_credit_limit_upto) values("
						+ loggedInOrgId
						+ ","
						+ loggedinUserId
						+ ","
						+ orgId
						+ ","
						+ extendedLimit
						+ ",'"
						+ new java.sql.Timestamp(new java.util.Date().getTime())
						+ "','" + new java.sql.Timestamp(d.getTime()) + "')";
				System.out.println("agent history " + updateHistoryQuery);
				stmt2.executeUpdate(updateHistoryQuery);

			}

			con.commit();

			// sending mail if
			if (emailBean != null && emailBean.getIsEmailSendControlFlag()) {
				MailSend mailSend = new MailSend(emailBean.getTo(), emailBean
						.getEmailMsg());
				mailSend.setDaemon(true);
				mailSend.start();
			}

		} catch (SQLException e) {

			e.printStackTrace();

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
			}

		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}

		}

		return availableCredit;
	}*/

	/**
	 * This method is used to get the organization details
	 * 
	 * @param orgid
	 *            organization Id (integer type)
	 * @return OrganizationBean
	 */
	public OrganizationBean orgDetails(int orgid) {

		int orgId = orgid;
		String countryCode = null;
		String stateCode = null;

		Connection con = null;

		try {

			OrganizationBean orgBean = null;

			orgBean = new OrganizationBean();

			System.out.println("heeeeeee");
			 

			Statement stmt2 = null;
			Statement stmt3 = null;

			con = DBConnect.getConnection();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			ResultSet rs2;
			System.out.println("org id is " + orgId);
			orgBean.setOrgId(orgId);

			String getOrgDetails = QueryManager.getST3OrgDetails()
					+ " where organization_id='" + orgId + "' ";
			rs2 = stmt2.executeQuery(getOrgDetails);
			// rs2=stmt2.executeQuery("select * from st_lms_organization_master
			// where organization_id='"+orgId+"'");
			while (rs2.next()) {

				orgBean.setOrgName(rs2.getString(TableConstants.ORG_NAME));
				orgBean.setOrgType(rs2.getString(TableConstants.ORG_TYPE));
				orgBean.setOrgAddr1(rs2.getString(TableConstants.ORG_ADDR1));
				orgBean.setOrgAddr2(rs2.getString(TableConstants.ORG_ADDR2));
				orgBean.setOrgCity(rs2.getString(TableConstants.ORG_CITY));
				orgBean.setOrgPin(rs2.getLong(TableConstants.ORG_PIN));
				orgBean.setSecDeposit(rs2.getDouble("security_deposit"));
				orgBean.setParentOrgId(rs2.getInt("parent_id"));
				orgBean.setOrgCreditLimit(rs2
						.getDouble(TableConstants.CREDIT_LIMIT));
				orgBean.setOrgStatus(rs2.getString(TableConstants.ORG_STATUS));
				orgBean.setCurrentCreditAmt(rs2
						.getDouble(TableConstants.SOM_CURR_CREDIT_AMT));
				orgBean.setExtendedCredit(rs2
						.getDouble(TableConstants.EXTENDED_CREDIT_LIMIT));
				orgBean.setAvailableCredit(rs2
						.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
				orgBean.setPwtScrapStatus(rs2.getString("pwt_scrap"));
				orgBean.setClaimableBal(rs2.getDouble("claimable_bal"));// added
				// by
				// amit
				countryCode = rs2.getString(TableConstants.ORG_COUNTRY);
				stateCode = rs2.getString(TableConstants.ORG_STATE);
				int extendedCreditLimitUpto = calculateExtendsCreditLimitUpto(rs2
						.getDate("extends_credit_limit_upto"));
				orgBean.setExtendsCreditLimitUpto(extendedCreditLimitUpto);

				String countryName = QueryManager.getCountryAndStateName()
						+ " cont.country_code='" + countryCode
						+ "' and stat.state_code='" + stateCode + "'";
				ResultSet countryList = stmt3.executeQuery(countryName);
				while (countryList.next()) {
					orgBean.setOrgCountry(countryList.getString("country"));
					orgBean.setOrgState(countryList.getString("state"));
				}

				/*
				 * String countryName=QueryManager.selectST3Country() + " where
				 * country_code='"+countryCode+"' " ; ResultSet
				 * countryList=stmt3.executeQuery(countryName);
				 * 
				 * //ResultSet countryList=stmt3.executeQuery("select name from
				 * st_lms_country_master where country_code='"+countryCode+"'");
				 * while(countryList.next()) {
				 * orgBean.setOrgCountry(countryList.getString(TableConstants.NAME)); }
				 * 
				 * String stateName=QueryManager.selectST3State() + " where
				 * state_code='"+stateCode+"' " ; ResultSet
				 * stateList=stmt3.executeQuery(stateName); //ResultSet
				 * stateList=stmt3.executeQuery("select name from
				 * st_lms_state_master where state_code='"+stateCode+"'");
				 * 
				 * while(stateList.next()) {
				 * orgBean.setOrgState(stateList.getString(TableConstants.NAME));
				 * System.out.println("state is "
				 * +stateList.getString(TableConstants.NAME)); }
				 * 
				 * //orgBean.setOrgCountry(rs2.getString(TableConstants.ORG_COUNTRY));
				 * //orgBean.setOrgState(rs2.getString(TableConstants.ORG_STATE));
				 */

			}
			System.out.println(orgBean.getParentOrgId()
					+ "******************test*******************");
			return orgBean;

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

		return null;

	}

	public void updateOrgScrapStatus(int orgid, String scrapStatus)
			throws LMSException {

		int orgId = orgid;
		Connection con = null;
		try {

			 
			Statement stmt1 = null;
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			stmt1 = con.createStatement();
			stmt1
					.executeUpdate("update st_lms_organization_master set pwt_scrap='"
							+ scrapStatus
							+ "' where organization_id="
							+ orgId
							+ "");
			con.commit();

		} catch (SQLException e) {

			e.printStackTrace();

			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
			}

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}

	}

}
