package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.HistoryBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.reportsMgmt.common.IPaymentLedgerReportAgtWiseHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.PaymentLedgerReportAgtWiseHelperSP;
import com.skilrock.lms.coreEngine.reportsMgmt.common.PaymentLedgerReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.PaymentLedgerRetailerWiseReportHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

/**
 * This class provides methods to get organization details and to edit
 * organization details
 * 
 * @author Skilrock Technologies
 * 
 */
public class OrganizationManagementHelper {
	private Log logger = LogFactory.getLog(OrganizationManagementHelper.class);

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
	 * @param scrapStatus
	 * @param daysAfter
	 * @param extendedLimit
	 */
	public String editOrgDetails(int userId, OrganizationBean previousOrgBean,
			String orgAddr1, String orgAddr2, String orgCity, long orgPin,
			String statusNew, double orgSecurityDeposit, double orgCreditLimit,
			String scrapStatus, String comment,String area,String division, int doneByUserId, String requestIp,String newIfuCode,String prevIfuCode) throws LMSException {
		String returnStatus = "";
		String reason = null;
		int orgId = previousOrgBean.getOrgId();

		String address1 = null;
		String address2 = null;
		String city = null;
		long pinCode = 0L;
		String divisionCode = null;
		String areaCode = null;
		String pwtScrap = null;
		double securityDeposit = 0.0;
		String orgStatus = null;
		int orgUserId = 0;
		String orgUserStatus = null;
		Connection con = null;
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			if (scrapStatus == null) {
				scrapStatus = previousOrgBean.getPwtScrapStatus().trim();
			}

			if(previousOrgBean.getArea()==null|| area==null){
				previousOrgBean.setArea("NA");
				area="NA";
			}

			int count = 0;
			stmt = con.createStatement();
			query = "SELECT addr_line1, addr_line2, city, pin_code, division_code, area_code, pwt_scrap, security_deposit, organization_status, user_id, status FROM st_lms_organization_master om INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id WHERE om.organization_id="+orgId+" AND isrolehead='Y';";
			logger.info("Seelct Org Details Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				address1 = rs.getString("addr_line1");
				address2 = rs.getString("addr_line2");
				city = rs.getString("city");
				pinCode = rs.getLong("pin_code");
				divisionCode = rs.getString("division_code");
				areaCode = rs.getString("area_code");
				pwtScrap = rs.getString("pwt_scrap");
				securityDeposit = rs.getDouble("security_deposit");
				orgStatus = rs.getString("organization_status");
				orgUserId = rs.getInt("user_id");
				orgUserStatus = rs.getString("status");
				count++;
			}
			if(count>1) {
				throw new LMSException("Invalid Organization Code.");
			}

			HistoryBean historyBean = new HistoryBean(orgId, doneByUserId, comment, requestIp);
			if(!address1.trim().equalsIgnoreCase(orgAddr1.trim())) {
				historyBean.setChangeType("ADDRESS_1");
				historyBean.setChangeValue(address1);
				historyBean.setUpdatedValue(orgAddr1);
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
			}
			if(!address2.trim().equalsIgnoreCase(orgAddr2.trim())) {
				historyBean.setChangeType("ADDRESS_2");
				historyBean.setChangeValue(address2);
				historyBean.setUpdatedValue(orgAddr2);
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
			}
			if(!city.trim().equalsIgnoreCase(orgCity.trim())) {
				historyBean.setChangeType("CITY");
				historyBean.setChangeValue(city);
				historyBean.setUpdatedValue(orgCity);
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
			}
			if(pinCode != orgPin) {
				historyBean.setChangeType("PIN_CODE");
				historyBean.setChangeValue(String.valueOf(pinCode));
				historyBean.setUpdatedValue(String.valueOf(orgPin));
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
			}
			if(divisionCode != null && !divisionCode.trim().equalsIgnoreCase(division.trim())) {
				historyBean.setChangeType("DIVISION_CODE");
				historyBean.setChangeValue(divisionCode);
				historyBean.setUpdatedValue(division);
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
			}
			if(areaCode != null && !areaCode.trim().equalsIgnoreCase(area.trim())) {
				historyBean.setChangeType("AREA_CODE");
				historyBean.setChangeValue(areaCode);
				historyBean.setUpdatedValue(area);
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
			}
			if(!pwtScrap.trim().equalsIgnoreCase(scrapStatus.trim())) {
				historyBean.setChangeType("PWT_SCRAP");
				historyBean.setChangeValue(pwtScrap);
				historyBean.setUpdatedValue(orgCity);
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
			}
			if(securityDeposit != orgSecurityDeposit) {
				historyBean.setChangeType("SECURITY_DEPOSIT");
				historyBean.setChangeValue(String.valueOf(securityDeposit));
				historyBean.setUpdatedValue(String.valueOf(orgSecurityDeposit));
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
			}
			if(!orgStatus.trim().equalsIgnoreCase(statusNew.trim())) {
				/*if(!orgStatus.trim().equalsIgnoreCase(statusNew.trim())) {
					reason = statusNew+"_MANUAL_BO";
				} else {
					reason = "CHANGE_ORG_INFO";
				}*/

				//historyBean.setComments(reason);
				historyBean.setChangeType("ORGANIZATION_STATUS");
				historyBean.setChangeValue(orgStatus);
				historyBean.setUpdatedValue(statusNew);
				CommonMethods.insertUpdateOrganizationHistory(historyBean, con);

				historyBean.setOrganizationId(orgUserId);
				historyBean.setChangeType("USER_STATUS");
				historyBean.setChangeValue(orgUserStatus);
				historyBean.setUpdatedValue(statusNew);
				CommonMethods.insertUpdateUserHistory(historyBean, con);
			}

			if (!"YES".equalsIgnoreCase(scrapStatus.trim()) && !previousOrgBean.getPwtScrapStatus().trim().equalsIgnoreCase(scrapStatus.trim())) {
				stmt = con.createStatement();
				query = "update st_lms_organization_master set pwt_scrap='NO' where parent_id = " + orgId + " and organization_type='RETAILER' and pwt_scrap='YES'";
				logger.info("Update Scrap Status Query - "+query);
				int updatedRows = stmt.executeUpdate(query);
				logger.info("Updated Rows - "+updatedRows);
			}
			
			if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
				String levyCat=newIfuCode.length()>0?"CAT-1":"CAT-2";
				
				if(!newIfuCode.trim().equalsIgnoreCase(prevIfuCode.trim())){
					if(newIfuCode.length()>0){
						Map<String,String> idMap=OrgNUserRegHelper.checkUniqueIdNo(newIfuCode.trim(), "IFU Code", con);
						if(idMap.containsKey("returnTypeError")){
							return "error";
						}
					}
					
					stmt=con.createStatement();
					query="UPDATE st_lms_user_contact_details SET id_type='IFU Code',id_nbr='"+newIfuCode+"' where user_id="+orgUserId;
					logger.info("Update Ifu Code Query - "+query);
					stmt.executeUpdate(query);
					query="UPDATE st_lms_organization_security_levy_master SET levy_cat_type='"+levyCat+" 'where organization_id="+orgId;
					stmt=con.createStatement();
					logger.info("Update leavy  category Query - "+query);
					stmt.executeUpdate(query);
				}
			}

			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeConnection(con, stmt, rs);
		}

		return returnStatus;
	}

	public OrganizationBean orgDetails(int orgid) {

		int orgId = orgid;
		String countryCode = null;
		String stateCode = null;
		Connection con = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
		ResultSet rs2=null;
		ResultSet idList=null;
		String getOrgDetails=null;
		try {

			OrganizationBean orgBean = null;
			orgBean = new OrganizationBean();
			con = DBConnect.getConnection();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			
			System.out.println("org id is " + orgId);
			orgBean.setOrgId(orgId);
			
			if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
				getOrgDetails = QueryManager.getST3OrgDetailsWithColletedSdDetail()
				+ " where om.organization_id='" + orgId + "' ";
	
			}else{
				getOrgDetails = QueryManager.getST3OrgDetails()
				+ " where organization_id='" + orgId + "' ";
			}
			rs2 = stmt2.executeQuery(getOrgDetails);
			// rs2=stmt2.executeQuery("select * from st_lms_organization_master
			// where organization_id='"+orgId+"'");
			while (rs2.next()) {

				orgBean.setOrgName(rs2.getString(TableConstants.ORG_NAME));
				orgBean.setOrgType(rs2.getString(TableConstants.ORG_TYPE));
				orgBean.setOrgAddr1(rs2.getString(TableConstants.ORG_ADDR1));
				orgBean.setOrgAddr2(rs2.getString(TableConstants.ORG_ADDR2));
				orgBean.setOrgCity(rs2.getString(TableConstants.ORG_CITY));
				orgBean.setArea(rs2.getString("area_code"));
				orgBean.setDivision(rs2.getString("division_code"));	
				orgBean.setParentOrgId(rs2
						.getInt(TableConstants.PARENT_ORGANIZATION_ID));
				orgBean.setCurrentCreditAmt(rs2
						.getDouble(TableConstants.SOM_CURR_CREDIT_AMT));
				orgBean.setExtendedCredit(rs2
						.getDouble(TableConstants.EXTENDED_CREDIT_LIMIT));
				orgBean
						.setExtendsCreditLimitUpto(calculateExtendsCreditLimitUpto(rs2
								.getDate("extends_credit_limit_upto")));
				orgBean.setAvailableCredit(rs2
						.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
				orgBean.setOrgPin(rs2.getLong(TableConstants.ORG_PIN));
				orgBean.setOrgCreditLimit(rs2
						.getDouble(TableConstants.CREDIT_LIMIT));
				orgBean.setOrgStatus(rs2.getString(TableConstants.ORG_STATUS));
				orgBean.setSecurityDeposit("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))?rs2.getDouble("collected_security_deposit"):rs2.getDouble("security_deposit"));
				orgBean.setPwtScrapStatus(rs2.getString("pwt_scrap"));
				orgBean.setClaimableBal(rs2.getDouble("claimable_bal"));
				countryCode = rs2.getString(TableConstants.ORG_COUNTRY);
				stateCode = rs2.getString(TableConstants.ORG_STATE);

				String countryName = QueryManager.getCountryAndStateName()
						+ " cont.country_code='" + countryCode
						+ "' and stat.state_code='" + stateCode + "'";
				ResultSet countryList = stmt3.executeQuery(countryName);
				while (countryList.next()) {
					orgBean.setOrgCountry(countryList.getString("country"));
					orgBean.setOrgState(countryList.getString("state"));
				}
				
				String idDetails="select  um.user_id,ucd.id_type,ucd.id_nbr  from st_lms_user_master um inner join st_lms_user_contact_details ucd  where um.user_id=ucd.user_id and organization_id="+orgid;
				idList=stmt3.executeQuery(idDetails);
				if(idList.next()){
					orgBean.setIdType(idList.getString("id_type"));
					if("IFU Code".equalsIgnoreCase(orgBean.getIdType())){
						orgBean.setIdNbr(idList.getString("id_nbr"));
					}
				}
				// check parent scrap status
				if ("RETAILER".equalsIgnoreCase(orgBean.getOrgType())) {
					String parentScrapStatusQuery = "select name, pwt_scrap, organization_type, organization_id  "
							+ "from st_lms_organization_master  where organization_id=(select parent_id  from "
							+ "st_lms_organization_master  where organization_id="
							+ orgId + ")";
					ResultSet parScrapStatusPstmt = stmt3
							.executeQuery(parentScrapStatusQuery);

					if (parScrapStatusPstmt.next()) {
						String parPwtScrap = parScrapStatusPstmt
								.getString("pwt_scrap");
						System.out.println(parentScrapStatusQuery
								+ "\n parent pwt scrap ==== " + parPwtScrap);
						orgBean.setParPwtScrap(parPwtScrap);
					}
				} else {
					orgBean.setParPwtScrap("YES");
				}

				// fetch limits of an organization
				CommonFunctionsHelper comm = new CommonFunctionsHelper();
				OrgPwtLimitBean limitBean = comm.fetchPwtLimitsOfOrgnization(
						orgId, con);
				orgBean.setVerLimit(FormatNumber.formatNumberForJSP(limitBean
						.getVerificationLimit()));
				orgBean.setAppLimit(FormatNumber.formatNumberForJSP(limitBean
						.getApprovalLimit()));
				orgBean.setPayLimit(FormatNumber.formatNumberForJSP(limitBean
						.getPayLimit()));
				orgBean.setScrapLimit(FormatNumber.formatNumberForJSP(limitBean
						.getScrapLimit()));

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
				 */
				// orgBean.setOrgCountry(rs2.getString(TableConstants.ORG_COUNTRY));
				// orgBean.setOrgState(rs2.getString(TableConstants.ORG_STATE));
			}

			return orgBean;

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, stmt3, idList);
			DBConnect.closeRs(rs2);
			DBConnect.closeStmt(stmt2);
		}

		return null;

	}

	public int fetchTerminalCount(int orgId) {
		int count = 0;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			/*rs = stmt.executeQuery("SELECT COUNT(serial_no) serial_count FROM st_lms_inv_status WHERE current_owner_id = "
							+ orgId + " GROUP BY current_owner_id;");*/
			rs = stmt.executeQuery("select (SELECT count(*) FROM st_lms_inv_status  a inner join st_lms_inv_model_master b on a.inv_model_id=b.model_id  WHERE current_owner_id = '"+ orgId +"' and b.inv_id=1) + " +
					"(SELECT count(*) FROM st_lms_inv_status inner join st_lms_organization_master on current_owner_id = organization_id  inner join st_lms_inv_model_master on inv_model_id=model_id where parent_id = '"+ orgId +"' and inv_id = 1)" +
					" serial_count;");
			
			if(rs.next())
				count = rs.getInt("serial_count");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return count;
	}
	
	
	
	public String getAgentOutstandingBal(int orgId, HttpServletRequest request, HttpSession session) throws ParseException, LMSException
	{

		
		
		 Calendar prevCal = Calendar.getInstance();
			/*String start_date = CommonMethods
					.convertDateInGlobalFormat(new java.sql.Date(prevCal
							.getTimeInMillis()).toString(), "yyyy-mm-dd",
							(String) session.getAttribute("date_format"));*/
			ServletContext sc = session.getServletContext();
			String dateFormat = (String) sc.getAttribute("date_format");
			String start_date = CommonMethods
			.convertDateInGlobalFormat(new java.sql.Date(prevCal
					.getTimeInMillis()).toString(), "yyyy-mm-dd",
					dateFormat);
			String deploy_Date = (String) sc.getAttribute("DEPLOYMENT_DATE");
			Timestamp startDate = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(start_date).getTime());
			Timestamp endDate = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(start_date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			Timestamp deployDate = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(deploy_Date).getTime());
			IPaymentLedgerReportAgtWiseHelper helper = null;
			if (LMSFilterDispatcher.isRepFrmSP) {
				helper = new PaymentLedgerReportAgtWiseHelperSP();
			} else {
				helper = new PaymentLedgerReportHelper();
			}
			/*boolean isDG = (Boolean) session.getAttribute("isDG") ;
			boolean isSE = (Boolean) session.getAttribute("isSE") ;
			boolean isCS = (Boolean) session.getAttribute("isCS") ;
			boolean isOLA = (Boolean) session.getAttribute("isOLA") ;*/
			
		 Map<String, CollectionReportOverAllBean> resultMap = helper
			.collectionAgentWiseWithOpeningBal(deployDate,
					startDate, endDate, orgId);
		 //#orgValue.dgSale-#orgValue.dgCancel-#orgValue.dgPwt-#orgValue.dgDirPlyPwt
		 
		 double drawCash = 0.0 ; //#orgValue.dgSale-#orgValue.dgCancel-#orgValue.dgPwt-#orgValue.dgDirPlyPwt
		 double scratchCash = 0.0 ; //#orgValue.seSale-#orgValue.seCancel-#orgValue.sePwt-#orgValue.seDirPlyPwt
		 double csSaleAmt = 0.0 ;
		 double csCancelAmt = 0.0 ;
		 double olaDepoAmt = 0.0 ; //#orgValue.deposit - #orgValue.depositRefund
		 double olaWdAmt = 0.0 ; //#orgValue.withdrawal - #orgValue.withdrawalRefund
		 double netGamingComm = 0.0 ;
		 double openingBal = 0.0 ;
		 double total = 0.0 ; 			 //#drawCash+#scratchCash-#agtNetPayment+#csSaleAmt-#csCancelAmt+#olaDepoAmt-#olaWdAmt-#netGamingComm2
		 double agentNetPayment = 0.0 ; //#orgValue.cash+#orgValue.cheque-#orgValue.chequeReturn-#orgValue.debit+#orgValue.credit+#orgValue.bankDep
		 double iwCash = 0.0;
		 double vsCash = 0.0;
		 for(Map.Entry<String, CollectionReportOverAllBean> str : resultMap.entrySet())
		 {
			 if(ReportUtility.isDG)
			 {
				 drawCash = str.getValue().getDgSale() -  str.getValue().getDgCancel() - str.getValue().getDgPwt() - str.getValue().getDgDirPlyPwt() ;
			 }
			 if(ReportUtility.isSE)
			 {
				 scratchCash = str.getValue().getSeSale() - str.getValue().getSeCancel() - str.getValue().getSePwt() - str.getValue().getSeDirPlyPwt() ;
			 }
			 if(ReportUtility.isCS)
			 {
				 csSaleAmt = str.getValue().getCSSale() ;
				 csCancelAmt = str.getValue().getCSCancel() ;
			 }
			 if(ReportUtility.isOLA)
			 {
				 olaDepoAmt = str.getValue().getDeposit() - str.getValue().getDepositRefund() ;
				 olaWdAmt = str.getValue().getWithdrawal() - str.getValue().getWithdrawalRefund() ;
				 netGamingComm = str.getValue().getNetGamingComm() ;
			 }
			 if(ReportUtility.isIW) {
				iwCash = str.getValue().getIwSale() - str.getValue().getIwCancel() - str.getValue().getIwPwt() - str.getValue().getIwDirPlyPwt();
			 }
			if (ReportUtility.isVS) {
				vsCash = str.getValue().getVsSale() - str.getValue().getVsCancel() - str.getValue().getVsPwt() - str.getValue().getVsDirPlyPwt();
			}
			agentNetPayment = str.getValue().getCash() + str.getValue().getCheque() - str.getValue().getChequeReturn() - str.getValue().getDebit() + str.getValue().getCredit() + str.getValue().getBankDep();
			total = drawCash + scratchCash - agentNetPayment + csSaleAmt - csCancelAmt + olaDepoAmt - olaWdAmt - netGamingComm + iwCash + vsCash;
			openingBal = str.getValue().getOpeningBal();
			 
		 }
		 //double drawCash = resultMap.get(orgId).getDgSale() - resultMap.get(orgId).getDgCancel() - resultMap.get(orgId).getDgPwt() - resultMap.get(orgId).getDgDirPlyPwt() ; 
		 double outBal = total + openingBal ;
		 
		 return String.valueOf(outBal) ;
		
		
		
	}
	public String getRetOutstandingBal(int retOrgId, HttpServletRequest request, HttpSession session, int agtOrgId) throws ParseException, LMSException
	{

		
		
		 Calendar prevCal = Calendar.getInstance();
			String start_date = CommonMethods
					.convertDateInGlobalFormat(new java.sql.Date(prevCal
							.getTimeInMillis()).toString(), "yyyy-mm-dd",
							(String) session.getAttribute("date_format"));
			ServletContext sc = session.getServletContext();
			String dateFormat = (String) sc.getAttribute("date_format");
			String deploy_Date = (String) sc.getAttribute("DEPLOYMENT_DATE");
			Timestamp startDate = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(start_date).getTime());
			Timestamp endDate = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(start_date).getTime()
					+ 24 * 60 * 60 * 1000 - 1000);
			Timestamp deployDate = new Timestamp((new SimpleDateFormat(dateFormat))
					.parse(deploy_Date).getTime());
			
			/*boolean isDG = (Boolean) session.getAttribute("isDG") ;
			boolean isSE = (Boolean) session.getAttribute("isSE") ;
			boolean isCS = (Boolean) session.getAttribute("isCS") ;
			boolean isOLA = (Boolean) session.getAttribute("isOLA") ;*/
			
			UserInfoBean userInfoBean = (UserInfoBean) session
			.getAttribute("USER_INFO");
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(ActionContext.getContext().getName());
			PaymentLedgerRetailerWiseReportHelper helper = new PaymentLedgerRetailerWiseReportHelper();
		 Map<String, CollectionReportOverAllBean> resultMap = helper
			.collectionRetailerWiseWithOpeningBal(deployDate,
					startDate, endDate, agtOrgId, retOrgId, reportStatusBean);
		 
		 double drawCash = 0.0 ; //#orgValue.dgSale-#orgValue.dgCancel-#orgValue.dgPwt-#orgValue.dgDirPlyPwt
		 double scratchCash = 0.0 ; //#orgValue.seSale-#orgValue.seCancel-#orgValue.sePwt-#orgValue.seDirPlyPwt
		 double csSaleAmt = 0.0 ;
		 double csCancelAmt = 0.0 ;
		 double olaDepoAmt = 0.0 ; //#orgValue.deposit - #orgValue.depositRefund
		 double olaWdAmt = 0.0 ; //#orgValue.withdrawal - #orgValue.withdrawalRefund
		 double netGamingComm = 0.0 ;
		 double openingBal = 0.0 ;
		 double total = 0.0 ; 			 //#drawCash+#scratchCash-#agtNetPayment+#csSaleAmt-#csCancelAmt+#olaDepoAmt-#olaWdAmt-#netGamingComm2
		 double agentNetPayment = 0.0 ; //#orgValue.cash+#orgValue.cheque-#orgValue.chequeReturn-#orgValue.debit+#orgValue.credit+#orgValue.bankDep
		 double iwCash = 0.0;
		 double vsCash = 0.0;
		 for(Map.Entry<String, CollectionReportOverAllBean> str : resultMap.entrySet())
		 {
			 if(ReportUtility.isDG)
			 {
				 drawCash = str.getValue().getDgSale() -  str.getValue().getDgCancel() - str.getValue().getDgPwt() - str.getValue().getDgDirPlyPwt() ;
			 }
			 if(ReportUtility.isSE)
			 {
				 scratchCash = str.getValue().getSeSale() - str.getValue().getSeCancel() - str.getValue().getSePwt() - str.getValue().getSeDirPlyPwt() ;
			 }
			 if(ReportUtility.isCS)
			 {
				 csSaleAmt = str.getValue().getCSSale() ;
				 csCancelAmt = str.getValue().getCSCancel() ;
			 }
			 if(ReportUtility.isOLA)
			 {
				 olaDepoAmt = str.getValue().getDeposit() - str.getValue().getDepositRefund() ;
				 olaWdAmt = str.getValue().getWithdrawal() - str.getValue().getWithdrawalRefund() ;
				 netGamingComm = str.getValue().getNetGamingComm() ;
			 }
			 if(ReportUtility.isIW) {
				iwCash = str.getValue().getIwSale() - str.getValue().getIwCancel() - str.getValue().getIwPwt() - str.getValue().getIwDirPlyPwt();
			 }
			 if(ReportUtility.isVS) {
				vsCash = str.getValue().getVsSale() - str.getValue().getVsCancel() - str.getValue().getVsPwt() - str.getValue().getVsDirPlyPwt();
			 }
			 agentNetPayment = str.getValue().getCash() + str.getValue().getCheque() - str.getValue().getChequeReturn() - str.getValue().getDebit() + str.getValue().getCredit() + str.getValue().getBankDep() ;
			 total = drawCash + scratchCash - agentNetPayment + csSaleAmt - csCancelAmt + olaDepoAmt - olaWdAmt - netGamingComm + iwCash + vsCash;
			 openingBal = str.getValue().getOpeningBal() ;
			 
		 }
		 //double drawCash = resultMap.get(orgId).getDgSale() - resultMap.get(orgId).getDgCancel() - resultMap.get(orgId).getDgPwt() - resultMap.get(orgId).getDgDirPlyPwt() ; 
		 double outBal = total + openingBal ;

		 return String.valueOf(outBal) ;
	}
}
