package com.skilrock.lms.common.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.EmailBean;
import com.skilrock.lms.beans.HistoryBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.accMgmt.serviceImpl.AgentAutoBlockServiceImpl;

public class OrgCreditUpdation {
	static Log logger = LogFactory.getLog(OrgCreditUpdation.class);

	private static final String projectName = LMSFilterDispatcher.projectName;

	/*
	public static synchronized boolean updateOrgSecurityDeposit(int orgId, double securityDeposit, Connection con) {

		boolean isValid = true;

		if(securityDeposit < 0)
			return false;

		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("update st_lms_organization_master set security_deposit=? where organization_id=?;");
			pstmt.setDouble(1, securityDeposit);
			pstmt.setDouble(2, orgId);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException se) {
			se.printStackTrace();
			isValid = false;
		}

		return isValid;
	}
	*/

	/**
	 * Created by sumit on 15 April 2013
	 * this function is used for Update balance of any organization and any transaction(Sale,Cash,CHEQUE)
	 * @param amount
	 * @param updateType
	 * @param transactionType
	 * @param orgId
	 * @param parentOrgId
	 * @param orgType
	 * @param noOfDays
	 * @param con
	 * @return
	 * @throws LMSException 
	 */
		public static boolean  updateOrganizationBalWithValidate(double amount,String updateType,String transactionType,
				int orgId, int parentOrgId, String orgType, int noOfDays,
				Connection con) throws LMSException {
			logger.info("Balance Logger 1: Amount="+amount+" ,updateType="+updateType+"," +
                    "transactionType="+transactionType+",orgId="+orgId+",parentOrgId"+parentOrgId+"orgType="+orgType+",noOfDays="+noOfDays);

			amount = "DEBIT".equals(transactionType) ? -1 * amount : amount;
			PreparedStatement updatePstmt = null;
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			
			double retMaxBalLimit = 0.0;
			boolean isValid = false;
			try {

				if ("AGENT".equals(orgType)) {
					if ("CLAIM_BAL".equalsIgnoreCase(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set claimable_bal = (claimable_bal+?)  where organization_id = ?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setInt(2, orgId);
						if ("CREDIT".equals(transactionType)) {
							
							if("GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
								isValid=true;
							}else{
								pstmt = con
										.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=? for update");
								pstmt.setInt(1, orgId);
								logger.info("Balance Logger 2:pstmt"+pstmt);
								rs = pstmt.executeQuery();
								if (rs.next()) {
									logger.info("Balance Logger 3:availbale_sale_bal"+rs.getDouble("availbale_sale_bal"));
									if (rs.getDouble("availbale_sale_bal") >= amount)
										isValid = true;
	
								}
							}
						}else
							isValid = true;

					} else if ("CL".equals(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set available_credit=available_credit+? ,credit_limit=credit_limit+?  where organization_id=?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setDouble(2, amount);
						updatePstmt.setInt(3, orgId);
						if ("DEBIT".equals(transactionType)) {
							pstmt = con
									.prepareStatement("select credit_limit+? cl from st_lms_organization_master where organization_id=? for update");
							pstmt.setDouble(1, amount);
							pstmt.setInt(2, orgId);
                            logger.info("Balance Logger 4:pstmt"+pstmt);
							rs = pstmt.executeQuery();
							if (rs.next()) {
                                logger.info("Balance Logger 5:cl"+rs.getDouble("cl"));
								if (rs.getDouble("cl") >= 0)
									isValid = true;
							}
						}else
							isValid = true;
					} else if ("XCL".equals(updateType)) {
						Calendar calendar = Calendar.getInstance();
						calendar.add(Calendar.DATE, noOfDays);
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set extended_credit_limit=extended_credit_limit+?,available_credit=available_credit+?,extends_credit_limit_upto=? where organization_id=?");

						updatePstmt.setDouble(1, amount);
						updatePstmt.setDouble(2, amount);
						updatePstmt.setTimestamp(3, new Timestamp(calendar.getTimeInMillis()));
						updatePstmt.setInt(4, orgId);
						isValid = true;

					} else if ("TRANSACTION".equals(updateType)) {

						if (transactionType.equals("SALE")
								|| transactionType.equals("CHQ_BOUNCE")
								|| transactionType.equals("DR_NOTE_CASH")
								|| transactionType.equals("OLA_DEPOSIT")
								|| transactionType.equals("LOOSE_SALE")
								|| transactionType.equals("DRAW_GAME_SALE")
								|| transactionType.equals("CS_SALE")
								|| transactionType.equals("SLE_SALE")) {
							updatePstmt = con
									.prepareStatement("update st_lms_organization_master set current_credit_amt =current_credit_amt+?, available_credit=available_credit-? where organization_id = ?");
							updatePstmt.setDouble(1, amount);
							updatePstmt.setDouble(2, amount);
							updatePstmt.setInt(3, orgId);
							isValid = true;
						} else if (transactionType.equals("SALE_RET")
								|| transactionType.equals("CASH")
								|| transactionType.equals("CHEQUE")
								|| transactionType.equals("PWT")
								|| transactionType.equals("PWT_AUTO")
								|| transactionType.equals("CASH_CHEQUE")
								|| transactionType.equals("CR_NOTE_CASH") // added
								// by
								// Arun
								|| transactionType.equals("BANK_DEPOSIT")
								|| transactionType.equals("OLA_WITHDRAWL")
								|| transactionType.equals("OLA_COMMISSION")
								|| transactionType.equals("LOOSE_SALE_RET")) {
							updatePstmt = con
									.prepareStatement("update st_lms_organization_master set current_credit_amt =current_credit_amt-?, available_credit=available_credit+? where organization_id = ?");
							updatePstmt.setDouble(1, amount);
							updatePstmt.setDouble(2, amount);
							updatePstmt.setInt(3, orgId);
							isValid = true;
						}else if ("IW_PWT".equals(transactionType)
								|| "IW_CANCEL".equals(transactionType) || "VS_CANCEL".equals(transactionType) || "VS_PWT".equals(transactionType)) {
							updatePstmt = con
									.prepareStatement("update st_lms_organization_master set current_credit_amt =current_credit_amt-?, available_credit=available_credit+? where organization_id = ?");
							updatePstmt.setDouble(1, amount);
							updatePstmt.setDouble(2, amount);
							updatePstmt.setInt(3, orgId);
							isValid = true;
						}else if ("IW_SALE".equals(transactionType) || "VS_SALE".equals(transactionType)) {
							updatePstmt = con
									.prepareStatement("update st_lms_organization_master set current_credit_amt =current_credit_amt+?, available_credit=available_credit-? where organization_id = ?");
							updatePstmt.setDouble(1, amount);
							updatePstmt.setDouble(2, amount);
							updatePstmt.setInt(3, orgId);
							pstmt = con
									.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=? for update");
							pstmt.setInt(1, orgId);
							logger.info("Balance Logger 28:pstmt"+pstmt);
							rs = pstmt.executeQuery();
							if (rs.next()) {
								logger.info("Balance Logger 28:availbale_sale_bal"+rs.getDouble("availbale_sale_bal"));
								if (rs.getDouble("availbale_sale_bal") >= amount)
									isValid = true;

							}
						}

					} else if ("UNCLAIM_BAL".equalsIgnoreCase(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set unclaimable_bal = (unclaimable_bal+?) where organization_id = ?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setInt(2, orgId);
						isValid = true;
					} else if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set claimable_bal = (claimable_bal-?), available_credit=(available_credit+?) where organization_id = ?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setDouble(2, amount);
						updatePstmt.setInt(3, orgId);
						isValid = true;
					} else if ("ACA_N_UNCLAIM_BAL".equalsIgnoreCase(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set unclaimable_bal = (unclaimable_bal-?), available_credit=(available_credit+?) where organization_id = ?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setDouble(2, amount);
						updatePstmt.setInt(3, orgId);
					}
					if (isValid){
						if(transactionType.equals("SALE")
								|| transactionType.equals("CHQ_BOUNCE")
								|| transactionType.equals("DR_NOTE_CASH")
								|| transactionType.equals("OLA_DEPOSIT")
								|| transactionType.equals("LOOSE_SALE")
								|| transactionType.equals("DRAW_GAME_SALE")
								|| transactionType.equals("CS_SALE")
								|| transactionType.equals("SLE_SALE")){
							// in case of 0
							/*pstmt=con.prepareStatement("select if(available_credit-claimable_bal-?>0,'ACTIVE','INACTIVE') status,organization_status from st_lms_organization_master where organization_id=?  and organization_status = 'INACTIVE';");
							pstmt.setDouble(1, amount);
							pstmt.setInt(2, orgId);
							rs=pstmt.executeQuery();
							if(rs.next()){
								if(!rs.getString("status").equals(rs.getString("organization_status"))){
									String comments="Organization becomes"+rs.getString("status")+" because Available Credit change by "+ transactionType+" With amount "+amount;
									HistoryBean historyBean=new HistoryBean(orgId, "ORGANIZATION_STATUS", rs.getString("organization_status"), rs.getString("status"), 0,comments, "");
									CommonMethods.insertUpdateOrganizationHistory(historyBean,con);
									
								}
							}*/
							
						}else if(transactionType.equals("SALE_RET")
								|| transactionType.equals("CASH")
								|| transactionType.equals("CHEQUE")
								|| transactionType.equals("PWT")
								|| transactionType.equals("PWT_AUTO")
								|| transactionType.equals("IW_PWT")
								|| transactionType.equals("CASH_CHEQUE")
								|| transactionType.equals("CR_NOTE_CASH") // added
								// by
								// Arun
								|| transactionType.equals("BANK_DEPOSIT")
								|| transactionType.equals("OLA_WITHDRAWL")
								|| transactionType.equals("OLA_COMMISSION")
								|| transactionType.equals("LOOSE_SALE_RET")
								|| "CL".equals(updateType)
								||"XCL".equals(updateType)
								||"DEBIT".equals(transactionType)){

							String countryDeployed = Utility.getPropertyValue("COUNTRY_DEPLOYED");
							if("NIGERIA".equals(countryDeployed)) {
								double amt=amount;
								if("CASH".equalsIgnoreCase(transactionType) || 
										"CHEQUE".equalsIgnoreCase(transactionType) ||
										"IW_PWT".equalsIgnoreCase(transactionType)  || "CASH_CHEQUE".equalsIgnoreCase(transactionType) 
										|| "CR_NOTE_CASH".equalsIgnoreCase(transactionType)  ||  "BANK_DEPOSIT".equalsIgnoreCase(transactionType)){
									amt=0.0;
									
								}
								// in case of 0
								//pstmt=con.prepareStatement("select if(available_credit-claimable_bal+?>0,'ACTIVE','INACTIVE') status,organization_status from st_lms_organization_master where organization_id=? and organization_status in ('ACTIVE','INACTIVE')");
								//pstmt=con.prepareStatement("SELECT IF((credit_limit-(available_credit-claimable_bal+?))>block_amt,'ACTIVE','INACTIVE') STATUS, organization_status FROM st_lms_organization_master om INNER JOIN st_lms_oranization_limits ol ON om.organization_id=ol.organization_id WHERE om.organization_id=? AND organization_status IN ('ACTIVE','INACTIVE');");
								//pstmt=con.prepareStatement("SELECT available_credit, claimable_bal, credit_limit, block_amt, IF(((available_credit-claimable_bal-credit_limit+?)) > (-block_amt),'ACTIVE','INACTIVE') STATUS, organization_status FROM st_lms_organization_master om INNER JOIN st_lms_oranization_limits ol ON om.organization_id=ol.organization_id WHERE om.organization_id=? AND organization_status ='INACTIVE';");
								//Query for current day sales of that agent on the basis of current day sales on/off

								double claimBalance = AgentAutoBlockServiceImpl.getInstance().getCBForPayment(orgId, con);
								logger.info("claimble Balance Calculated - "+claimBalance);

								pstmt = con.prepareStatement("SELECT available_credit, claimable_bal, credit_limit, extended_credit_limit, extends_credit_limit_upto, block_amt, IF(((available_credit-?-?-credit_limit-extended_credit_limit+?)) > (-block_amt),'ACTIVE','INACTIVE') STATUS, organization_status FROM st_lms_organization_master om INNER JOIN st_lms_oranization_limits ol ON om.organization_id=ol.organization_id WHERE om.organization_id=? AND organization_status ='INACTIVE' for update;");
								pstmt.setDouble(1, claimBalance);
								pstmt.setDouble(2, amt);
								pstmt.setDouble(3, amount);
								pstmt.setInt(4, orgId);
								logger.info("Query - "+pstmt);
								rs = pstmt.executeQuery();
								if(rs.next()) {
									logger.info("-- Inside If Condition --");
									logger.info("Organization Id - "+orgId);
									logger.info("Amount - "+amount);
									logger.info("Available Credit - "+rs.getString("available_credit"));
									logger.info("Claimble Balance - "+rs.getString("claimable_bal"));
									logger.info("Credit Limit - "+rs.getString("credit_limit"));
									logger.info("Extended Credit Limit - "+rs.getString("extended_credit_limit"));
									logger.info("Extended Credit Limit Upto - "+rs.getString("extends_credit_limit_upto"));
									logger.info("Block Amount - "+rs.getString("block_amt"));
									logger.info("Status - "+rs.getString("status"));
									logger.info("Organization Status - "+rs.getString("organization_status"));

									if(!rs.getString("status").equals(rs.getString("organization_status"))) {
										logger.info("-- Status Not Match, Inside If --");
										String comments="Organization becomes"+rs.getString("status")+" because Available Credit change by "+ transactionType+" With amount "+amount;
										HistoryBean historyBean=new HistoryBean(orgId, "ORGANIZATION_STATUS", rs.getString("organization_status"), rs.getString("status"), 0,comments, "");
										CommonMethods.insertUpdateOrganizationHistory(historyBean,con);
									} else
										logger.info("-- Status Match, Inside Else --");
								} else
									logger.info("-- Inside Else Condition --");
							}
						}
						isValid = updatePstmt.executeUpdate()>0;
					}

				} else if ("RETAILER".equals(orgType)) {
					if ("CLAIM_BAL".equalsIgnoreCase(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set claimable_bal = (claimable_bal+?)  where organization_id = ?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setInt(2, orgId);
						if ("CREDIT".equals(transactionType)) {
							pstmt = con
									.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=? for update;");
							pstmt.setInt(1, orgId);
                            logger.info("Balance Logger 6:pstmt"+pstmt);
							rs = pstmt.executeQuery();
							if (rs.next()) {
                                logger.info("Balance Logger 7:availbale_sale_bal"+rs.getDouble("availbale_sale_bal"));
								if (rs.getDouble("availbale_sale_bal") >= amount)
									isValid = true;
							}
						}else
							isValid = true;

					} else if ("CL".equals(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set available_credit=available_credit+? ,credit_limit=credit_limit+?  where organization_id=?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setDouble(2, amount);
						updatePstmt.setInt(3, orgId);
						if ("DEBIT".equals(transactionType)) {
							pstmt = con
									.prepareStatement("select credit_limit+? cl from st_lms_organization_master where organization_id=? for update;");
							pstmt.setDouble(1, amount);
							pstmt.setInt(2, orgId);
                            logger.info("Balance Logger 8:pstmt"+pstmt);
							rs = pstmt.executeQuery();
							if (rs.next()) {
                                logger.info("Balance Logger 9:cl"+rs.getDouble("cl"));
								if (rs.getDouble("cl") >= 0)
									isValid = true;
							}
						}else
							isValid = true;
					} else if ("XCL".equals(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set extended_credit_limit=extended_credit_limit+?,available_credit=available_credit+?,extends_credit_limit_upto=? where organization_id=?");

						updatePstmt.setDouble(1, amount);
						updatePstmt.setDouble(2, amount);
						updatePstmt.setTimestamp(3, new Timestamp(System
								.currentTimeMillis()
								+ 86400 * 1000 * noOfDays));
						updatePstmt.setInt(4, orgId);
						isValid = true;
					} else if ("TRANSACTION".equals(updateType)) {

						if (transactionType.equals("SALE")
								|| transactionType.equals("CHQ_BOUNCE")
								|| transactionType.equals("DR_NOTE_CASH")
								|| transactionType.equals("OLA_DEPOSIT")
								|| transactionType.equals("LOOSE_SALE")
								|| transactionType.equals("DRAW_GAME_SALE")
								|| transactionType.equals("CS_SALE")
								|| transactionType.equals("SLE_SALE")) {
							updatePstmt = con
									.prepareStatement("update st_lms_organization_master set current_credit_amt =current_credit_amt+?, available_credit=available_credit-? where organization_id = ?");
							updatePstmt.setDouble(1, amount);
							updatePstmt.setDouble(2, amount);
							updatePstmt.setInt(3, orgId);
							isValid = true;

						} else if (transactionType.equals("SALE_RET")
								|| transactionType.equals("CASH")
								|| transactionType.equals("CHEQUE")
								|| transactionType.equals("PWT")
								|| transactionType.equals("PWT_AUTO")
								|| transactionType.equals("CASH_CHEQUE")
								|| transactionType.equals("CR_NOTE_CASH")
								|| transactionType.equals("BANK_DEPOSIT")
								|| transactionType.equals("OLA_WITHDRAWL")
								|| transactionType.equals("OLA_COMMISSION")
								|| transactionType.equals("LOOSE_SALE_RET")) {
							updatePstmt = con
									.prepareStatement("update st_lms_organization_master set current_credit_amt =current_credit_amt-?, available_credit=available_credit+? where organization_id = ?");
							updatePstmt.setDouble(1, amount);
							updatePstmt.setDouble(2, amount);
							updatePstmt.setInt(3, orgId);
							
							if("PWT_AUTO".equals(transactionType) || "GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
								isValid = true;
							}else{

								pstmt = con
										.prepareStatement("select ifnull(slom.available_credit-slom.claimable_bal,0)-ifnull(retbal.bal,0) as totalbal from st_lms_organization_master slom,(select sum(retBal.retBal) as bal from (select if(available_credit-claimable_bal>0,available_credit-claimable_bal,0) as retBal from st_lms_organization_master where parent_id=? ) as retBal) as retbal where slom.organization_id=? for update;");
								pstmt.setInt(1, parentOrgId);
								pstmt.setInt(2, parentOrgId);
								logger.info("Bal Query::" + pstmt);
								rs = pstmt.executeQuery();
								if (rs.next()) {
									retMaxBalLimit = rs.getDouble("totalbal");
                                    logger.info("Balance Logger 10:retMaxBalLimit"+retMaxBalLimit);
								}
								if (retMaxBalLimit >= amount) {
									isValid = true;
									}
								
								
							}
						}else if ("IW_SALE".equals(transactionType) || "VS_SALE".equals(transactionType)) {
							updatePstmt = con
									.prepareStatement("update st_lms_organization_master set current_credit_amt =current_credit_amt+?, available_credit=available_credit-? where organization_id = ?");
							updatePstmt.setDouble(1, amount);
							updatePstmt.setDouble(2, amount);
							updatePstmt.setInt(3, orgId);
							pstmt = con
									.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=? for update;");
							pstmt.setInt(1, orgId);
                            logger.info("Balance Logger 27:pstmt"+pstmt);
							rs = pstmt.executeQuery();
							if (rs.next()) {
                                logger.info("Balance Logger 27:availbale_sale_bal"+rs.getDouble("availbale_sale_bal"));
								if (rs.getDouble("availbale_sale_bal") >= amount)
									isValid = true;
							}
														
						}
						else if ("IW_PWT".equals(transactionType)
								|| "IW_CANCEL".equals(transactionType) || "VS_CANCEL".equals(transactionType) || "VS_PWT".equals(transactionType)) {
							updatePstmt = con
									.prepareStatement("update st_lms_organization_master set current_credit_amt =current_credit_amt-?, available_credit=available_credit+? where organization_id = ?");
							updatePstmt.setDouble(1, amount);
							updatePstmt.setDouble(2, amount);
							updatePstmt.setInt(3, orgId);							
							isValid = true;
							
						}

					} else if ("UNCLAIM_BAL".equalsIgnoreCase(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set unclaimable_bal = (unclaimable_bal+?) where organization_id = ?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setInt(2, orgId);
						isValid = true;
					} else if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set claimable_bal = (claimable_bal-?), available_credit=(available_credit+?) where organization_id = ?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setDouble(2, amount);
						updatePstmt.setInt(3, orgId);
						isValid = true;
					} else if ("ACA_N_UNCLAIM_BAL".equalsIgnoreCase(updateType)) {
						updatePstmt = con
								.prepareStatement("update st_lms_organization_master set unclaimable_bal = (unclaimable_bal-?), available_credit=(available_credit+?) where organization_id = ?");
						updatePstmt.setDouble(1, amount);
						updatePstmt.setDouble(2, amount);
						updatePstmt.setInt(3, orgId);
						isValid = true;
					}
					if (isValid){
						if(transactionType.equals("SALE")
								|| transactionType.equals("CHQ_BOUNCE")
								|| transactionType.equals("DR_NOTE_CASH")
								|| transactionType.equals("OLA_DEPOSIT")
								|| transactionType.equals("LOOSE_SALE")
								|| transactionType.equals("DRAW_GAME_SALE")
								|| transactionType.equals("CS_SALE")){
							
							/*pstmt=con.prepareStatement("select if(available_credit-claimable_bal-?>0,'ACTIVE','INACTIVE') status,organization_status from st_lms_organization_master where organization_id=? and organization_status = 'INACTIVE';");
							pstmt.setDouble(1, amount);
							pstmt.setInt(2, orgId);
							rs=pstmt.executeQuery();
							if(rs.next()){
								if(!rs.getString("status").equals(rs.getString("organization_status"))){
									String comments="Organization becomes"+rs.getString("status")+" because Available Credit change by "+ transactionType+" With amount "+amount;
									HistoryBean historyBean=new HistoryBean(orgId, "ORGANIZATION_STATUS", rs.getString("organization_status"), rs.getString("status"), 0,comments, "");
									CommonMethods.insertUpdateOrganizationHistory(historyBean,con);
									
								}
							}*/
														
						}else if(transactionType.equals("SALE_RET")
								|| transactionType.equals("CASH")
								|| transactionType.equals("CHEQUE")
								|| transactionType.equals("PWT")
								|| transactionType.equals("PWT_AUTO")
								|| transactionType.equals("CASH_CHEQUE")
								|| transactionType.equals("CR_NOTE_CASH")
								|| transactionType.equals("BANK_DEPOSIT")
								|| transactionType.equals("OLA_WITHDRAWL")
								|| transactionType.equals("OLA_COMMISSION")
								|| transactionType.equals("LOOSE_SALE_RET")
								|| "CL".equals(updateType)
								||"XCL".equals(updateType)){

							
							//pstmt=con.prepareStatement("select if(available_credit-claimable_bal+?>0,'ACTIVE','INACTIVE') status,organization_status from st_lms_organization_master where organization_id=? and organization_status in ('ACTIVE','INACTIVE')");
							pstmt=con.prepareStatement("select if(available_credit-claimable_bal+?>0,'ACTIVE','INACTIVE') status,organization_status from st_lms_organization_master where organization_id=? and organization_status = 'INACTIVE' for update;");
							pstmt.setDouble(1, amount);
							pstmt.setInt(2, orgId);
                            logger.info("Balance Logger 11:pstmt"+pstmt);
							rs=pstmt.executeQuery();
							if(rs.next()){
								if(!rs.getString("status").equals(rs.getString("organization_status"))){
									String comments="Organization becomes"+rs.getString("status")+" because Available Credit change by "+ transactionType+" With amount "+amount;
									HistoryBean historyBean=new HistoryBean(orgId, "ORGANIZATION_STATUS", rs.getString("organization_status"), rs.getString("status"), 0,comments, "");
									CommonMethods.insertUpdateOrganizationHistory(historyBean,con);
									
								}
							}
											
						}
                        logger.info("Balance Logger 12:updatePstmt"+updatePstmt);
						isValid= updatePstmt.executeUpdate()>0;
					}
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException();
				//isValid=false;
			}
			if(!isValid){
				logger.debug("AMOUNT_ERROR:  Org Id="+orgId+" = amount="+amount +" = updateType="+updateType+" =transaction Type="+transactionType+"=Parent Org Id="+parentOrgId);
				throw new LMSException();
			}
			return isValid;

		}
		
		
	
	
	
	
	
	
	
	
	/**
	 * This Function check for (ACA-ClaimableBal)<=0 and return a Map which
	 * contain two Entries. 1. Key is "ORG_STATUS" and value, if Balance is
	 * negative or Zero than status 'INACTIVE' otherwise 'ACTIVE' 2. key is
	 * 'EMAIL_BEAN' and value contain a Object of EmailBean which contain
	 * emailMsg and receiver's mail id
	 * 
	 * @param orgId
	 * @param connection
	 * @param creditLimit
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Object> fetchOrgStatusWithMailMsg(int orgId,
			Connection connection, double availableCredit) throws SQLException {

		String orgStatus = "INACTIVE";
		boolean mailSendControlFlag = false;
		EmailBean emailBean = new EmailBean();

		String emailMsgTxt = "", email = "";

		String emailQuery = "select a.email_id,a.first_name,a.last_name,b.organization_status, b.claimable_bal from st_lms_user_contact_details a,st_lms_organization_master b where user_id=(select user_id from st_lms_user_master where organization_id='"
				+ orgId
				+ "' and  isrolehead='Y') and b.organization_id="
				+ orgId;
		Statement statement = connection.createStatement();
		ResultSet set = statement.executeQuery(emailQuery);
		if (set.next()) {
			email = set.getString("email_id");
			String firstName = set.getString("first_name");
			String lastName = set.getString("last_name");
			String currentStatus = set.getString("organization_status");
			double claimableBal = set.getDouble("claimable_bal");
			double availableCreditLimit = availableCredit - claimableBal;

			if (availableCreditLimit <= 0) {
				orgStatus = "INACTIVE";
				if ("ACTIVE".equalsIgnoreCase(currentStatus)) {
					String msgFor = "Your  account has been temporarily suspended because of insufficient Credit Limit.  \n Your Account Balance is: "
							+ availableCreditLimit
							+ " "
							+ LMSFilterDispatcher.currencySymbol;
					emailMsgTxt = "<html><table><tr><td>Hi "
							+ firstName
							+ " "
							+ lastName
							+ "</td></tr><tr><td>"
							+ msgFor
							+ "</td></tr></table><table><tr><td align='right'>log on </td><td align='left'>"
							+ LMSFilterDispatcher.webLink + projectName
							+ "/</td></tr></table></html>";
					mailSendControlFlag = true;
				}
			} else {
				orgStatus = "ACTIVE";
				if ("INACTIVE".equalsIgnoreCase(currentStatus)) {
					String msgFor = "Your  account has been Activated. \n Your Account Balance is: "
							+ availableCreditLimit
							+ " "
							+ LMSFilterDispatcher.currencySymbol;
					emailMsgTxt = "<html><table><tr><td>Hi "
							+ firstName
							+ " "
							+ lastName
							+ "</td></tr><tr><td>"
							+ msgFor
							+ "</td></tr></table><table><tr><td align='right'>log on </td><td align='left'>"
							+ LMSFilterDispatcher.webLink + projectName
							+ "/</td></tr></table></html>";
					mailSendControlFlag = true;
				}
			}
		}

		emailBean.setEmailMsg(emailMsgTxt);
		emailBean.setTo(email);
		emailBean.setEmailSendControlFlag(mailSendControlFlag);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ORG_STATUS", orgStatus);
		map.put("EMAIL_BEAN", emailBean);
		return map;
	}

	public synchronized static boolean updateCreditLimitForAgent(int orgId,
			String transactionType, double amount, Connection connection)
			throws SQLException, LMSException {

		logger.debug("Agent ###########" + orgId);

		PreparedStatement orgPstmt = null;
		PreparedStatement orgUpdatePstmt = null;
		PreparedStatement orgHistPstmt = null;
		String emailQuery = null;
		Statement statement = null;
		ResultSet set = null;
		String email = null;
		String firstName = null;
		String lastName = null;
		String currentStatus = null;
		String comment = null;
		String reason = null;

		ResultSet resultSet = null;

		String orgQuery = null;
		String orgUpdateQuery = null;
		HttpSession session = ServletActionContext.getRequest().getSession(false);
		int parentUserId = 0;
		if (session != null) {
			parentUserId = ((UserInfoBean) session.getAttribute("USER_INFO"))
					.getUserId();
		}
		double currCreditAmt = 0.0;
		double creditLimit = 0.0;
		double newCreditAmt = 0.0;
		double newCreditLimit = 0.0;
		double clamableBal = 0.0;
		boolean isOrgAvailable = false;
		boolean isUpdateDone = false;

		String orgType = null;

		orgQuery = QueryManager.getST1OrgCreditQuery();
		orgPstmt = connection.prepareStatement(orgQuery);

		orgPstmt.setInt(1, orgId);
		resultSet = orgPstmt.executeQuery();

		while (resultSet.next()) {
			isOrgAvailable = true;

			orgType = resultSet.getString(TableConstants.SOM_ORG_TYPE);
			currCreditAmt = resultSet
					.getDouble(TableConstants.SOM_CURR_CREDIT_AMT);
			creditLimit = resultSet
					.getDouble(TableConstants.SOM_AVAILABLE_CREDIT);
		}

		if (isOrgAvailable) {

			if (orgType != null && !orgType.equals("AGENT")) {
				throw new LMSException("Not a Valid Organization Type");
			}

			if (transactionType.equals("SALE")
					|| transactionType.equals("CHQ_BOUNCE")
					|| transactionType.equals("DR_NOTE_CASH") || transactionType.equals("OLA_DEPOSIT") || transactionType.equals("LOOSE_SALE")) {

				newCreditAmt = currCreditAmt + amount;
				newCreditLimit = creditLimit - amount;

			} else if (transactionType.equals("SALE_RET")
					|| transactionType.equals("CASH")
					|| transactionType.equals("CHEQUE")
					|| transactionType.equals("PWT")
					|| transactionType.equals("PWT_AUTO")
					|| transactionType.equals("CASH_CHEQUE")
					|| transactionType.equals("CR_NOTE_CASH") // added by Arun
					|| transactionType.equals("BANK_DEPOSIT")
					|| transactionType.equals("OLA_WITHDRAWL")
					|| transactionType.equals("OLA_COMMISSION")
					|| transactionType.equals("LOOSE_SALE_RET")
			) {
				newCreditAmt = currCreditAmt - amount;
				newCreditLimit = creditLimit + amount;

			} else if (transactionType.equals("DRAW_GAME_SALE")||transactionType.equals("CS_SALE")) {
				newCreditAmt = currCreditAmt + amount;
				newCreditLimit = creditLimit - amount;
			}

			logger.debug(transactionType + " New newCreditLimit: Agent"
					+ newCreditLimit);
			logger.debug(transactionType + " New Credit Amt: Agent"
					+ newCreditAmt);
			logger.debug(transactionType + " New currCreditAmt: Agent"
					+ currCreditAmt);
			logger.debug((newCreditAmt >= newCreditLimit) + "Amt: Agent"
					+ amount);
			orgUpdateQuery = QueryManager.getST1OrgCreditUpdateQuery();
			orgUpdatePstmt = connection.prepareStatement(orgUpdateQuery);

			emailQuery = "select a.email_id,a.first_name,a.last_name,b.organization_status, b.claimable_bal "
					+ " from st_lms_user_contact_details a,st_lms_organization_master b where a.user_id = (select "
					+ " user_id from st_lms_user_master where organization_id='"
					+ orgId
					+ "'and isrolehead='Y') and b.organization_id="
					+ orgId;

			statement = connection.createStatement();
			set = statement.executeQuery(emailQuery);
			while (set.next()) {
				email = set.getString("email_id");
				firstName = set.getString("first_name");
				lastName = set.getString("last_name");
				currentStatus = set.getString("organization_status");
				clamableBal = set.getDouble("claimable_bal");

				orgUpdatePstmt.setDouble(1, newCreditAmt);
				if (newCreditLimit - clamableBal <= 0
						&& "ACTIVE".equalsIgnoreCase(currentStatus)) {
					orgUpdatePstmt.setString(2, "INACTIVE");
					comment = "Organization becomes INACTIVE because Available Credit goes Negative by "
							+ transactionType;
					reason = "INACTIVE_AUTO_ACT";
					/*String query = "insert into st_lms_organization_master_history select '"
							+ parentUserId
							+ "',organization_id, addr_line1, addr_line2,division_code,area_code, city, pin_code, security_deposit, credit_limit,'"
							+ reason
							+ "', '"
							+ comment
							+ "','INACTIVE','"
							+ new java.sql.Timestamp(new java.util.Date()
									.getTime())
							+ "', pwt_scrap, recon_report_type  from st_lms_organization_master where organization_id = "
							+ orgId;
					orgHistPstmt = connection.prepareStatement(query);
					orgHistPstmt.executeUpdate();*/
					String msgFor = "Your  account has been temporarily suspended because of insufficient Credit Limit. \n Your Account Balance is: "
							+ (newCreditLimit - clamableBal)
							+ " "
							+ LMSFilterDispatcher.currencySymbol;
					String emailMsgTxt = "<html><table><tr><td>Hi "
							+ firstName
							+ " "
							+ lastName
							+ "</td></tr><tr><td>"
							+ msgFor
							+ "</td></tr></table><table><tr><td align='right'>log on </td><td align='left'>"
							+ LMSFilterDispatcher.webLink + projectName
							+ "/</td></tr></table></html>";
					MailSend mailSend = new MailSend(email, emailMsgTxt);
					mailSend.setDaemon(true);
					mailSend.start();

					// MailSend.sendMail(email,firstName,lastName,msgFor);
				} else if (newCreditLimit - clamableBal <= 0
						&& "INACTIVE".equalsIgnoreCase(currentStatus)) {
					orgUpdatePstmt.setString(2, "INACTIVE");
					comment = "Organization becomes INACTIVE because Available Credit goes Negative by "
							+ transactionType;
					reason = "INACTIVE_AUTO_ACT";
			/*		String query = "insert into st_lms_organization_master_history select '"
							+ parentUserId
							+ "',organization_id, addr_line1, addr_line2,division_code,area_code, city, pin_code, security_deposit, credit_limit,'"
							+ reason
							+ "', '"
							+ comment
							+ "','INACTIVE','"
							+ new java.sql.Timestamp(new java.util.Date()
									.getTime())
							+ "', pwt_scrap, recon_report_type  from st_lms_organization_master where organization_id = "
							+ orgId;
					orgHistPstmt = connection.prepareStatement(query);
					orgHistPstmt.executeUpdate();*/
					String msgFor = "Your  account has been temporarily suspended because of insufficient Credit Limit. \n Your Account Balance is: "
							+ (newCreditLimit - clamableBal)
							+ " "
							+ LMSFilterDispatcher.currencySymbol;
					String emailMsgTxt = "<html><table><tr><td>Hi "
							+ firstName
							+ " "
							+ lastName
							+ "</td></tr><tr><td>"
							+ msgFor
							+ "</td></tr></table><table><tr><td align='right'>log on </td><td align='left'>"
							+ LMSFilterDispatcher.webLink + projectName
							+ "/</td></tr></table></html>";
					MailSend mailSend = new MailSend(email, emailMsgTxt);
					mailSend.setDaemon(true);
					mailSend.start();

					// MailSend.sendMail(email,firstName,lastName,msgFor);
				} else if (!"TERMINATE".equalsIgnoreCase(currentStatus)) {
					orgUpdatePstmt.setString(2, "ACTIVE");
					if ("INACTIVE".equalsIgnoreCase(currentStatus)) {
						comment = "Organization becomes ACTIVE because Available Credit goes Positive by "
								+ transactionType;
						reason = "ACTIVE_AUTO_ACT";
		/*				String query = "insert into st_lms_organization_master_history select '"
								+ parentUserId
								+ "',organization_id, addr_line1, addr_line2,division_code,area_code,city,  pin_code, security_deposit, credit_limit,'"
								+ reason
								+ "', '"
								+ comment
								+ "','ACTIVE','"
								+ new java.sql.Timestamp(new java.util.Date()
										.getTime())
								+ "', pwt_scrap, recon_report_type  from st_lms_organization_master where organization_id = "
								+ orgId;
						orgHistPstmt = connection.prepareStatement(query);
						orgHistPstmt.executeUpdate();*/
						String msgFor = "Your  account has been Activated. \n Your Account Balance is: "
								+ (newCreditLimit - clamableBal)
								+ " "
								+ LMSFilterDispatcher.currencySymbol;
						String emailMsgTxt = "<html><table><tr><td>Hi "
								+ firstName
								+ " "
								+ lastName
								+ "</td></tr><tr><td>"
								+ msgFor
								+ "</td></tr></table><table><tr><td align='right'>log on </td><td align='left'>"
								+ LMSFilterDispatcher.webLink + projectName
								+ "/</td></tr></table></html>";
						MailSend mailSend = new MailSend(email, emailMsgTxt);
						mailSend.setDaemon(true);
						mailSend.start();
						// MailSend.sendMail(email,firstName,lastName,msgFor);
					}
				}
				if ("TERMINATE".equalsIgnoreCase(currentStatus)) {
					orgUpdatePstmt.setString(2, currentStatus);
				}
				orgUpdatePstmt.setDouble(3, newCreditLimit);

				orgUpdatePstmt.setInt(4, orgId);
				isUpdateDone = orgUpdatePstmt.execute();

			}

		}

		return isUpdateDone;
	}

	public static boolean updateCreditLimitForRetailer(int orgId,
			String transactionType, double amount, Connection connection)
			throws SQLException, LMSException {

		logger.debug("Retailer  ###########" + orgId);
		HttpSession session = ServletActionContext.getRequest().getSession(false);
		PreparedStatement orgPstmt = null;
		PreparedStatement orgUpdatePstmt = null;
		//PreparedStatement orgHistPstmt = null;

		ResultSet resultSet = null;

		String orgQuery = null;
		String orgUpdateQuery = null;

		double currCreditAmt = 0.0;
		double creditLimit = 0.0;
		double newCreditAmt = 0.0;
		double newCreditLimit = 0.0;
		boolean isOrgAvailable = false;
		boolean isUpdateDone = false;
		String currentStatus = null;
		String comment = null;
		String reason = null;
		double clamableBal = 0.0;
		int parentUserId = 0; // For Update Ledger By Cron Job
		if (session != null) {
			parentUserId = ((UserInfoBean) session.getAttribute("USER_INFO"))
					.getUserId();
		}
		String emailQuery = null;
		Statement statement = null;
		ResultSet set = null;
		String email = null;
		String firstName = null;
		String lastName = null;

		String orgType = null;

		orgQuery = QueryManager.getST1OrgCreditQuery();
		orgPstmt = connection.prepareStatement(orgQuery);

		orgPstmt.setInt(1, orgId);
		resultSet = orgPstmt.executeQuery();

		while (resultSet.next()) {
			isOrgAvailable = true;

			orgType = resultSet.getString(TableConstants.SOM_ORG_TYPE);
			currCreditAmt = resultSet
					.getDouble(TableConstants.SOM_CURR_CREDIT_AMT);
			creditLimit = resultSet
					.getDouble(TableConstants.SOM_AVAILABLE_CREDIT);
		}

		if (isOrgAvailable) {

			if (orgType != null && !orgType.equals("RETAILER")) {
				throw new LMSException("Not a Valid Organization Type");
			}

			if (transactionType.equals("SALE")
					|| transactionType.equals("CHQ_BOUNCE")
					|| transactionType.equals("DR_NOTE_CASH") || transactionType.equals("OLA_DEPOSIT")||transactionType.equals("LOOSE_SALE")) {

				newCreditAmt = currCreditAmt + amount;
				newCreditLimit = creditLimit - amount;

			} else if (transactionType.equals("SALE_RET")
					|| transactionType.equals("CASH")
					|| transactionType.equals("CHEQUE")
					|| transactionType.equals("PWT")
					|| transactionType.equals("PWT_AUTO")
					|| transactionType.equals("CASH_CHEQUE")
					|| transactionType.equals("CR_NOTE_CASH") || transactionType.equals("OLA_COMMISSION") || transactionType.equals("OLA_WITHDRAWL")
					|| transactionType.equals("LOOSE_SALE_RET")) {// @amit

				newCreditAmt = currCreditAmt - amount;
				newCreditLimit = creditLimit + amount;

			} else if (transactionType.equals("DRAW_GAME_SALE") || transactionType.equals("CS_SALE")) {
				newCreditAmt = currCreditAmt + amount;
				newCreditLimit = creditLimit - amount;
			}
			logger.debug("New Credit Amt: RETailer" + newCreditAmt);
			logger.debug("New currCreditAmt: RETailer" + currCreditAmt);
			logger.debug("Amt: RETailer" + amount);
			logger.debug("New Credit Amt: RETailer" + newCreditAmt);
			orgUpdateQuery = QueryManager.getST1OrgCreditUpdateQuery();
			orgUpdatePstmt = connection.prepareStatement(orgUpdateQuery);
			emailQuery = "select a.email_id,a.first_name,a.last_name,b.organization_status, b.claimable_bal from st_lms_user_contact_details a,st_lms_organization_master b where user_id=(select user_id from st_lms_user_master where organization_id='"
					+ orgId
					+ "' and  isrolehead='Y') and b.organization_id="
					+ orgId;
			statement = connection.createStatement();
			set = statement.executeQuery(emailQuery);
			while (set.next()) {
				email = set.getString("email_id");
				firstName = set.getString("first_name");
				lastName = set.getString("last_name");
				currentStatus = set.getString("organization_status");
				clamableBal = set.getDouble("claimable_bal");
			}

			orgUpdatePstmt.setDouble(1, newCreditAmt);
			if (newCreditLimit - clamableBal <= 0) {
				orgUpdatePstmt.setString(2, "INACTIVE");
				if ("ACTIVE".equalsIgnoreCase(currentStatus)) {
					comment = "Organization becomes INACTIVE because Available Credit goes Negative by "
							+ transactionType;
					reason = "INACTIVE_AUTO_ACT";
			/*		String query = "insert into st_lms_organization_master_history select '"
							+ parentUserId
							+ "',organization_id, addr_line1, addr_line2,division_code,area_code, city,  pin_code, security_deposit, credit_limit,'"
							+ reason
							+ "', '"
							+ comment
							+ "','INACTIVE','"
							+ new java.sql.Timestamp(new java.util.Date()
									.getTime())
							+ "', pwt_scrap, recon_report_type  from st_lms_organization_master where organization_id = "
							+ orgId;
					orgHistPstmt = connection.prepareStatement(query);
					orgHistPstmt.executeUpdate();*/
					String msgFor = "Your  account has been temporarily suspended because of insufficient Credit Limit.  \n Your Account Balance is: "
							+ (newCreditLimit - clamableBal)
							+ " "
							+ LMSFilterDispatcher.currencySymbol;
					String emailMsgTxt = "<html><table><tr><td>Hi "
							+ firstName
							+ " "
							+ lastName
							+ "</td></tr><tr><td>"
							+ msgFor
							+ "</td></tr></table><table><tr><td align='right'>log on </td><td align='left'>"
							+ LMSFilterDispatcher.webLink + projectName
							+ "/</td></tr></table></html>";
					MailSend mailSend = new MailSend(email, emailMsgTxt);
					mailSend.setDaemon(true);
					mailSend.start();
				}
				// MailSend.sendMail(email,firstName,lastName,msgFor);
			} else {
				orgUpdatePstmt.setString(2, "ACTIVE");
				if ("INACTIVE".equalsIgnoreCase(currentStatus)) {
					comment = "Organization becomes ACTIVE because Available Credit goes Positive by "
							+ transactionType;
					reason = "ACTIVE_AUTO_ACT";
					/*String query = "insert into st_lms_organization_master_history select '"
							+ parentUserId
							+ "',organization_id, addr_line1, addr_line2,division_code,area_code, city,  pin_code, security_deposit, credit_limit,'"
							+ reason
							+ "', '"
							+ comment
							+ "','ACTIVE','"
							+ new java.sql.Timestamp(new java.util.Date()
									.getTime())
							+ "', pwt_scrap, recon_report_type  from st_lms_organization_master where organization_id = "
							+ orgId;
					orgHistPstmt = connection.prepareStatement(query);
					orgHistPstmt.executeUpdate();*/
					String msgFor = "Your  account has been Activated. \n Your Account Balance is: "
							+ (newCreditLimit - clamableBal)
							+ " "
							+ LMSFilterDispatcher.currencySymbol;
					String emailMsgTxt = "<html><table><tr><td>Hi "
							+ firstName
							+ " "
							+ lastName
							+ "</td></tr><tr><td>"
							+ msgFor
							+ "</td></tr></table><table><tr><td align='right'>log on </td><td align='left'>"
							+ LMSFilterDispatcher.webLink + projectName
							+ "/</td></tr></table></html>";
					MailSend mailSend = new MailSend(email, emailMsgTxt);
					mailSend.setDaemon(true);
					mailSend.start();
					// MailSend.sendMail(email,firstName,lastName,msgFor);
					// MailSend.sendMail(email,firstName,lastName,msgFor);
				}
			}

			orgUpdatePstmt.setDouble(3, newCreditLimit);
			orgUpdatePstmt.setInt(4, orgId);
			isUpdateDone = orgUpdatePstmt.execute();

		}

		return isUpdateDone;
	}
}
