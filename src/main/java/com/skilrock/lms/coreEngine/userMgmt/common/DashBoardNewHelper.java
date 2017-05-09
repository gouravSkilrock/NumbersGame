package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.skilrock.lms.beans.DashBoardBean;
import com.skilrock.lms.beans.LimitDistributionBean;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.drawGames.common.Util;

public class DashBoardNewHelper extends LocalizedTextUtil {

	static Log logger = LogFactory.getLog(DashBoardNewHelper.class);
	
	private Locale locale;

	public DashBoardNewHelper() {
		locale = Locale.getDefault();
	}

	public static void main(String[] args) throws LMSException {
		new DashBoardNewHelper().fetchMenuData();
	}

	public List<Object> fetchMenuData() throws LMSException {
		Connection con = null;
		List<Object> dashBoardList = new ArrayList<Object>();
		Map<String, DashBoardBean> map = new LinkedHashMap<String, DashBoardBean>();
		con = DBConnect.getConnection();
		// LimitDistributionBean limitBean = new LimitDistributionBean();
		// dashBoardList.add(limitBean);
		dashBoardList.add(map);
		DashBoardBean tempBean = null;
		String orgCodeQry = " name orgCode ";

		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = "  org_code orgCode ";

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(org_code,'_',name)  orgCode ";

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = " concat(name,'_',org_code)  orgCode ";

		}
		String childDataQry = "select organization_id,"
				+ orgCodeQry
				+ ",city,organization_status,(available_credit-claimable_bal) as balance, extended_credit_limit, extends_credit_limit_upto from st_lms_organization_master where organization_type='AGENT' and organization_status <> 'TERMINATE' order by "
				+ QueryManager.getAppendOrgOrder();
		try {
			PreparedStatement pstmt = con.prepareStatement(childDataQry);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				tempBean = new DashBoardBean();
				tempBean.setOrgId(rs.getInt("organization_id"));
				tempBean.setOrgName(rs.getString("orgCode"));
				tempBean.setOrgStatus(rs.getString("organization_status"));
				tempBean.setLocation(rs.getString("city"));
				tempBean.setBalance(rs.getString("balance"));
				tempBean.setXclAmt(rs.getString("extended_credit_limit"));
				// String xclDays = rs.getString("extends_credit_limit_upto");
				int extendedCreditLimitUpto = calculateExtendsCreditLimitUpto(rs
						.getDate("extends_credit_limit_upto"));
				tempBean.setXclDays(extendedCreditLimitUpto + "");

				map.put(rs.getInt("organization_id") + "", tempBean);
			}
			// fetchParentBalance(con, rs, pstmt, limitBean);
			/*
			 * String retloginStatusQry =
			 * "select organization_id,offline_status,is_offline,terminal_id from st_lms_ret_offline_master where organization_id in (select organization_id from st_lms_organization_master where parent_id="
			 * + agentOrgId + " )"; pstmt =
			 * con.prepareStatement(retloginStatusQry); rs =
			 * pstmt.executeQuery();
			 * 
			 * while (rs.next()) { tempBean =
			 * map.get(rs.getString("organization_id"));
			 * tempBean.setDeviceName(rs.getString("terminal_id"));
			 * tempBean.setWebType(rs.getString("is_offline"));
			 * tempBean.setOfflineStatus(rs.getString("offline_status")); }
			 */
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		// System.out.println(map);
		return dashBoardList;
	}

	private void fetchParentBalance(Connection con, ResultSet rs,
			PreparedStatement pstmt, LimitDistributionBean limitBean)
			throws SQLException {
		String parentBalQry = "select organization_id,name, credit_limit, extended_credit_limit, live_balance, distributed ,ifnull((live_balance - distributed),0) as distributable from (select organization_id, name, organization_type, credit_limit, extended_credit_limit,(available_credit-claimable_bal)as live_balance from st_lms_organization_master where organization_type='AGENT'";

		pstmt = con.prepareStatement(parentBalQry);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			limitBean.setOrgId(rs.getInt("organization_id"));
			limitBean.setName(rs.getString("name"));
			limitBean.setCrLimit(rs.getString("credit_limit"));
			limitBean.setXcrLimit(rs.getString("extended_credit_limit"));
			limitBean.setLiveBal(rs.getString("live_balance"));
			limitBean.setDistributedBal(rs.getString("distributed"));
			limitBean.setDistributableBal(rs.getString("distributable"));
		}
	}

	public List<Object> updateAgentData(String[] retOrgId, String[] balance,
			String[] orgStatus, UserInfoBean userbean, String[] xclAmt,
			String[] xclDays) throws LMSException {
		Connection con = null;
		List<Object> dashBoardList = null;
		Map<String, String> map = new LinkedHashMap<String, String>();
		con = DBConnect.getConnection();
		// dashBoardList.add(map);

		PreparedStatement getOrgPstmt = null;
		ResultSet getOrgRs = null;
		OrganizationBean orgBean = null;
		PreparedStatement updateOrgPstmt = null;
		PreparedStatement insertHistoryPstmt = null;

		try {

			AgentManagementHelper editOrgDetail = new AgentManagementHelper();

			for (int i = 0; i < retOrgId.length; i++) {
				con.setAutoCommit(false);

				getOrgPstmt = con
						.prepareStatement("select organization_id,parent_id,organization_type,name,available_credit,claimable_bal,unclaimable_bal,credit_limit,security_deposit,organization_status,extended_credit_limit,current_credit_amt,extends_credit_limit_upto from st_lms_organization_master where organization_id=?");
				getOrgPstmt.setInt(1, Integer.parseInt(retOrgId[i]));
				getOrgRs = getOrgPstmt.executeQuery();
				if (getOrgRs.next()) {
					orgBean = new OrganizationBean();
					orgBean.setOrgId(getOrgRs.getInt("organization_id"));
					orgBean.setParentOrgId(getOrgRs.getInt("parent_id"));
					orgBean.setOrgName(getOrgRs
							.getString(TableConstants.ORG_NAME));
					orgBean.setOrgType(getOrgRs
							.getString(TableConstants.ORG_TYPE));
					orgBean.setSecDeposit(getOrgRs
							.getDouble("security_deposit"));
					orgBean.setOrgCreditLimit(getOrgRs
							.getDouble(TableConstants.CREDIT_LIMIT));
					orgBean.setOrgStatus(getOrgRs
							.getString(TableConstants.ORG_STATUS));
					orgBean.setCurrentCreditAmt(getOrgRs
							.getDouble(TableConstants.SOM_CURR_CREDIT_AMT));
					orgBean.setExtendedCredit(getOrgRs
							.getDouble(TableConstants.EXTENDED_CREDIT_LIMIT));
					orgBean.setAvailableCredit(getOrgRs
							.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
					orgBean
							.setClaimableBal(getOrgRs
									.getDouble("claimable_bal"));
					int extendedCreditLimitUpto = calculateExtendsCreditLimitUpto(getOrgRs
							.getDate("extends_credit_limit_upto"));
					orgBean.setExtendsCreditLimitUpto(extendedCreditLimitUpto);

				}

				getOrgPstmt = con
						.prepareStatement("select login_status from st_lms_ret_offline_master where organization_id= ? and is_offline='YES'");
				getOrgPstmt.setInt(1, Integer.parseInt(retOrgId[i]));
				getOrgRs = getOrgPstmt.executeQuery();
				if (getOrgRs.next()) {

					map.put(retOrgId[i], findDefaultText("msg.offline.ret.login", locale));
					continue;
				} else {

					if (!orgBean.getOrgStatus().equals(orgStatus[i])) {

						if (orgStatus[i].equalsIgnoreCase("ACTIVE")) {
							if ((orgBean.getAvailableCredit()
									- orgBean.getClaimableBal() + Double
									.parseDouble(balance[i])) < 0) {
								map
										.put(
												retOrgId[i],
												findDefaultText("msg.status.cannot.be.active.on.neg.bal", locale));
								continue;
							}
						}
						updateOrgPstmt = con
								.prepareStatement("update st_lms_organization_master set organization_status=? where organization_id=?");
						updateOrgPstmt.setString(1, orgStatus[i]);
						updateOrgPstmt.setInt(2, Integer.parseInt(retOrgId[i]));
						updateOrgPstmt.executeUpdate();

						insertHistoryPstmt = con
								.prepareStatement("insert into st_lms_organization_master_history select ?,organization_id, addr_line1, addr_line2,division_code,area_code, city, pin_code, security_deposit, credit_limit,?,?,organization_status,?, pwt_scrap, recon_report_type  from st_lms_organization_master where organization_id =?");
						insertHistoryPstmt.setInt(1, userbean.getUserId());
						insertHistoryPstmt.setString(2, orgStatus[i]
								+ "_MANUAL_"
								+ userbean.getRoleName().split(" ")[0]);
						insertHistoryPstmt.setString(3, "Organization made "
								+ orgStatus[i] + " by "
								+ userbean.getRoleName().split(" ")[0] + ": "
								+ userbean.getOrgName());
						insertHistoryPstmt.setTimestamp(4, Util
								.getCurrentTimeStamp());
						insertHistoryPstmt.setInt(5, Integer
								.parseInt(retOrgId[i]));
						insertHistoryPstmt.executeUpdate();

					}

					if (Double.parseDouble(balance[i]) != 0) {

						String agtBalDist = (orgBean.getOrgCreditLimit()
								+ Double.parseDouble(balance[i]) >= 0) ? "TRUE"
								: findDefaultText("msg.cannot.dec.more.than.agt.lmt", locale);

						System.out.println(agtBalDist + "******" + retOrgId[i]);
						if ("TRUE".equals(agtBalDist)) {
							editOrgDetail.editOrgCreditLimitDetails(orgBean,
									Double.parseDouble(balance[i]), userbean,
									con);
							map.put(retOrgId[i], findDefaultText("msg.record.update.success", locale));
						} else {
							map.put(retOrgId[i], agtBalDist);
							continue;
						}
					}
					// map.put(retOrgId[i], "Record Updated Successfully.");
					// ------
					/*
					 * if(Double.parseDouble(xclAmt[i]) >= 0 &&
					 * Integer.parseInt(xclDays[i]) > 0 &&
					 * orgBean.getExtendedCredit()-
					 * Double.parseDouble(xclAmt[i])>=0){
					 * 
					 * 
					 * String agtBalDist = "TRUE";
					 * getOrgPstmt=con.prepareStatement(
					 * "select organization_id,parent_id,organization_type,name,available_credit,claimable_bal,unclaimable_bal,credit_limit,security_deposit,organization_status,extended_credit_limit,current_credit_amt,extends_credit_limit_upto from st_lms_organization_master where organization_id=?"
					 * ); getOrgPstmt.setInt(1, Integer .parseInt(retOrgId[i]));
					 * getOrgRs=getOrgPstmt.executeQuery(); if(getOrgRs.next()){
					 * orgBean = new OrganizationBean();
					 * orgBean.setOrgId(getOrgRs.getInt("organization_id"));
					 * orgBean.setParentOrgId(getOrgRs.getInt("parent_id"));
					 * orgBean
					 * .setOrgName(getOrgRs.getString(TableConstants.ORG_NAME));
					 * orgBean
					 * .setOrgType(getOrgRs.getString(TableConstants.ORG_TYPE));
					 * orgBean
					 * .setSecDeposit(getOrgRs.getDouble("security_deposit"));
					 * orgBean.setOrgCreditLimit(getOrgRs
					 * .getDouble(TableConstants.CREDIT_LIMIT));
					 * orgBean.setOrgStatus
					 * (getOrgRs.getString(TableConstants.ORG_STATUS));
					 * orgBean.setCurrentCreditAmt(getOrgRs
					 * .getDouble(TableConstants.SOM_CURR_CREDIT_AMT));
					 * orgBean.setExtendedCredit(getOrgRs
					 * .getDouble(TableConstants.EXTENDED_CREDIT_LIMIT));
					 * orgBean.setAvailableCredit(getOrgRs
					 * .getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
					 * orgBean.
					 * setClaimableBal(getOrgRs.getDouble("claimable_bal")); int
					 * extendedCreditLimitUpto =
					 * calculateExtendsCreditLimitUpto(getOrgRs
					 * .getDate("extends_credit_limit_upto"));
					 * orgBean.setExtendsCreditLimitUpto
					 * (extendedCreditLimitUpto);
					 * 
					 * }
					 */
					/*
					 * agtBalDist = CommonMethods.changeCreditLimitRet(Integer
					 * .parseInt(retOrgId[i]), bean .getOrgCreditLimit() +
					 * Double.parseDouble(xclAmt[i]), "XCL");
					 */
					/*
					 * System.out.println(agtBalDist + "******" + retOrgId[i]);
					 * 
					 * if (agtBalDist.equals("TRUE")) {
					 * editOrgDetail.editOrgDetails(
					 * Double.parseDouble(xclAmt[i]),
					 * Integer.parseInt(xclDays[i]), userbean,orgBean,con);
					 * 
					 * } else { map.put(retOrgId[i], agtBalDist); }
					 * 
					 * } else if((Double.parseDouble(xclAmt[i]) == 0 ||
					 * Double.parseDouble(xclAmt[i])<0) &&
					 * Integer.parseInt(xclDays[i])>0){ map.put(retOrgId[i],
					 * "Please Select Valid XCL Amount");
					 * 
					 * } else if((Integer.parseInt(xclDays[i]) == 0 ||
					 * Integer.parseInt(xclDays[i])<0) &&
					 * Double.parseDouble(xclAmt[i])>0){ map.put(retOrgId[i],
					 * "Please Select Valid XCL Days"); }else
					 * if(Double.parseDouble(xclAmt[i]) < 0 &&
					 * Integer.parseInt(xclDays[i]) < 0){ map.put(retOrgId[i],
					 * "Please Select Valid XCL Amount");
					 * 
					 * }
					 */

					if ((orgBean.getExtendedCredit()
							- Double.parseDouble(xclAmt[i]) != 0)
							|| orgBean.getExtendsCreditLimitUpto()
									- Integer.parseInt(xclDays[i]) != 0) {
						if (orgBean.getExtendedCredit() > 0) {
							if ((Double.parseDouble(xclAmt[i]) > 0 && Integer
									.parseInt(xclDays[i]) > 0)
									|| (Double.parseDouble(xclAmt[i]) == 0 && Integer
											.parseInt(xclDays[i]) == 0)) {

								String agtBalDist = "TRUE";
								getOrgPstmt = con
										.prepareStatement("select organization_id,parent_id,organization_type,name,available_credit,claimable_bal,unclaimable_bal,credit_limit,security_deposit,organization_status,extended_credit_limit,current_credit_amt,extends_credit_limit_upto from st_lms_organization_master where organization_id=?");
								getOrgPstmt.setInt(1, Integer
										.parseInt(retOrgId[i]));
								getOrgRs = getOrgPstmt.executeQuery();
								if (getOrgRs.next()) {
									orgBean = new OrganizationBean();
									orgBean.setOrgId(getOrgRs
											.getInt("organization_id"));
									orgBean.setParentOrgId(getOrgRs
											.getInt("parent_id"));
									orgBean
											.setOrgName(getOrgRs
													.getString(TableConstants.ORG_NAME));
									orgBean
											.setOrgType(getOrgRs
													.getString(TableConstants.ORG_TYPE));
									orgBean.setSecDeposit(getOrgRs
											.getDouble("security_deposit"));
									orgBean
											.setOrgCreditLimit(getOrgRs
													.getDouble(TableConstants.CREDIT_LIMIT));
									orgBean
											.setOrgStatus(getOrgRs
													.getString(TableConstants.ORG_STATUS));
									orgBean
											.setCurrentCreditAmt(getOrgRs
													.getDouble(TableConstants.SOM_CURR_CREDIT_AMT));
									orgBean
											.setExtendedCredit(getOrgRs
													.getDouble(TableConstants.EXTENDED_CREDIT_LIMIT));
									orgBean
											.setAvailableCredit(getOrgRs
													.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
									orgBean.setClaimableBal(getOrgRs
											.getDouble("claimable_bal"));
									int extendedCreditLimitUpto = calculateExtendsCreditLimitUpto(getOrgRs
											.getDate("extends_credit_limit_upto"));
									orgBean
											.setExtendsCreditLimitUpto(extendedCreditLimitUpto);

								}
								/*
								 * agtBalDist =
								 * CommonMethods.changeCreditLimitRet(Integer
								 * .parseInt(retOrgId[i]), bean
								 * .getOrgCreditLimit() +
								 * Double.parseDouble(xclAmt[i]), "XCL");
								 */
								System.out.println(agtBalDist + "******"
										+ retOrgId[i]);

								if (agtBalDist.equals("TRUE")) {
									editOrgDetail.editOrgDetails(Double
											.parseDouble(xclAmt[i]), Integer
											.parseInt(xclDays[i]), userbean,
											orgBean, con);
									map
											.put(
													retOrgId[i],
													findDefaultText("msg.record.update.success", locale));
								} else {
									map.put(retOrgId[i], agtBalDist);
								}
							} else {
								map.put(retOrgId[i], findDefaultText("msg.select.valid.xcl.entry", locale));

							}
						} else {
							if (Double.parseDouble(xclAmt[i]) > 0
									&& Integer.parseInt(xclDays[i]) > 0) {

								String agtBalDist = "TRUE";
								getOrgPstmt = con
										.prepareStatement("select organization_id,parent_id,organization_type,name,available_credit,claimable_bal,unclaimable_bal,credit_limit,security_deposit,organization_status,extended_credit_limit,current_credit_amt,extends_credit_limit_upto from st_lms_organization_master where organization_id=?");
								getOrgPstmt.setInt(1, Integer
										.parseInt(retOrgId[i]));
								getOrgRs = getOrgPstmt.executeQuery();
								if (getOrgRs.next()) {
									orgBean = new OrganizationBean();
									orgBean.setOrgId(getOrgRs
											.getInt("organization_id"));
									orgBean.setParentOrgId(getOrgRs
											.getInt("parent_id"));
									orgBean
											.setOrgName(getOrgRs
													.getString(TableConstants.ORG_NAME));
									orgBean
											.setOrgType(getOrgRs
													.getString(TableConstants.ORG_TYPE));
									orgBean.setSecDeposit(getOrgRs
											.getDouble("security_deposit"));
									orgBean
											.setOrgCreditLimit(getOrgRs
													.getDouble(TableConstants.CREDIT_LIMIT));
									orgBean
											.setOrgStatus(getOrgRs
													.getString(TableConstants.ORG_STATUS));
									orgBean
											.setCurrentCreditAmt(getOrgRs
													.getDouble(TableConstants.SOM_CURR_CREDIT_AMT));
									orgBean
											.setExtendedCredit(getOrgRs
													.getDouble(TableConstants.EXTENDED_CREDIT_LIMIT));
									orgBean
											.setAvailableCredit(getOrgRs
													.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
									orgBean.setClaimableBal(getOrgRs
											.getDouble("claimable_bal"));
									int extendedCreditLimitUpto = calculateExtendsCreditLimitUpto(getOrgRs
											.getDate("extends_credit_limit_upto"));
									orgBean
											.setExtendsCreditLimitUpto(extendedCreditLimitUpto);

								}
								/*
								 * agtBalDist =
								 * CommonMethods.changeCreditLimitRet(Integer
								 * .parseInt(retOrgId[i]), bean
								 * .getOrgCreditLimit() +
								 * Double.parseDouble(xclAmt[i]), "XCL");
								 */
								System.out.println(agtBalDist + "******"
										+ retOrgId[i]);

								if (agtBalDist.equals("TRUE")) {
									editOrgDetail.editOrgDetails(Double
											.parseDouble(xclAmt[i]), Integer
											.parseInt(xclDays[i]), userbean,
											orgBean, con);
									map
											.put(
													retOrgId[i],
													findDefaultText("msg.record.update.success", locale));
								} else {
									map.put(retOrgId[i], agtBalDist);
								}
							} else {
								map.put(retOrgId[i], findDefaultText("msg.select.valid.xcl.entry", locale));
							}
						}
					}
					/*
					 * 
					 * updHistXCL = false; // Hard-Coding to be removed if
					 * (updHistXCL) { String msg = ""; if(updStatus){ msg =
					 * "Organization made "+orgStatus[i]+" by "+
					 * userbean.getRoleName().split(" ")[0]
					 * +": "+userbean.getOrgName(); stmtHistory.addBatch(
					 * "insert into st_lms_organization_master_history select "
					 * + userbean.getUserId() +
					 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'"
					 * + orgStatus[i] +"_MANUAL_"+
					 * userbean.getRoleName().split(" ")[0] +"','"+ msg
					 * +"', organization_status,'"+ new java.sql.Timestamp(new
					 * java.util.Date().getTime()).toString()+
					 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
					 * + retOrgId[i]);
					 * 
					 * System.out.println(
					 * "insert into st_lms_organization_master_history select "
					 * + userbean.getUserId() +
					 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'"
					 * + orgStatus[i] +"_MANUAL_"+
					 * userbean.getRoleName().split(" ")[0] +"','"+ msg
					 * +"', organization_status,'"+ new java.sql.Timestamp(new
					 * java.util.Date().getTime()).toString()+
					 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
					 * + retOrgId[i]); } else{ msg = "credit "+ balance[i];
					 * stmtHistory.addBatch(
					 * "insert into st_lms_organization_master_history select "
					 * + userbean.getUserId() +
					 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'CREDIT_CHANGED_"
					 * + userbean.getRoleName().split(" ")[0] +"','"+ msg
					 * +"', organization_status,'"+ new java.sql.Timestamp(new
					 * java.util.Date().getTime()).toString()+
					 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
					 * + retOrgId[i]);System.out.println(
					 * "insert into st_lms_organization_master_history select "
					 * + userbean.getUserId() +
					 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'CREDIT_CHANGED_"
					 * + userbean.getRoleName().split(" ")[0] +"','"+ msg
					 * +"', organization_status,'"+ new java.sql.Timestamp(new
					 * java.util.Date().getTime()).toString()+
					 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
					 * + retOrgId[i]); }
					 * 
					 * }
					 */

				}

				/*
				 * AgentManagementHelper editOrgDetail = new
				 * AgentManagementHelper(); OrganizationBean bean =
				 * editOrgDetail.orgDetails(Integer .parseInt(retOrgId[i]));
				 * updHist = false; updStatus = false;
				 * 
				 * updHistXCL = false; if
				 * (!DrawGameOfflineHelper.fetchLoginStatus(Integer
				 * .parseInt(retOrgId[i]))) {
				 * 
				 * if (!bean.getOrgStatus().equals(orgStatus[i])) { updHist =
				 * true; updStatus = true;
				 * 
				 * updHistXCL = true; if
				 * (orgStatus[i].equalsIgnoreCase("ACTIVE")) { if
				 * ((bean.getAvailableCredit() - bean.getClaimableBal() + Double
				 * .parseDouble(balance[i])) < 0) { updHist = false; updHistXCL
				 * = false; map .put(retOrgId[i],
				 * "Status Can't be made ACTIVE on -ve balance"); } } if
				 * (updStatus) { stmt.addBatch(
				 * "update st_lms_organization_master set organization_status='"
				 * + orgStatus[i] + "' where organization_id=" + retOrgId[i] +
				 * ""); } }
				 * 
				 * 
				 * if (Double.parseDouble(balance[i]) != 0) { updHist = true;
				 * updStatus = false; String
				 * agtBalDist=(bean.getOrgCreditLimit()
				 * +Double.parseDouble(balance
				 * [i])>=0)?"TRUE":"Can't  Decrease More than AGENT's limit";
				 * //String agtBalDist = "TRUE"; String agtBalDist =
				 * CommonMethods.changeCreditLimitAgt(Integer
				 * .parseInt(retOrgId[i]), bean .getOrgCreditLimit() +
				 * Double.parseDouble(balance[i]), "CL");
				 * System.out.println(agtBalDist + "******" + retOrgId[i]); if
				 * (agtBalDist.equals("TRUE")) {
				 * editOrgDetail.editOrgCreditLimitDetails(bean .getOrgId(),
				 * bean.getOrgCreditLimit() + Double.parseDouble(balance[i]),
				 * bean .getSecDeposit(), bean .getCurrentCreditAmt(), bean
				 * .getExtendedCredit(), userbean .getUserType(),
				 * userbean.getUserOrgId(), userbean.getUserId()); } else {
				 * map.put(retOrgId[i], agtBalDist); updHist=false; } }
				 * 
				 * //------ if(Double.parseDouble(xclAmt[i]) != 0 &&
				 * Integer.parseInt(xclDays[i]) != 0){ updHistXCL = true;
				 * updStatus = false;
				 * 
				 * String agtBalDist = "TRUE"; bean =
				 * editOrgDetail.orgDetails(Integer.parseInt(retOrgId[i]));
				 * agtBalDist = CommonMethods.changeCreditLimitRet(Integer
				 * .parseInt(retOrgId[i]), bean .getOrgCreditLimit() +
				 * Double.parseDouble(xclAmt[i]), "XCL");
				 * System.out.println(agtBalDist + "******" + retOrgId[i]); if
				 * (agtBalDist.equals("TRUE")) {
				 * editOrgDetail.editOrgDetails(bean.getOrgId(),
				 * Double.parseDouble(xclAmt[i]), bean.getCurrentCreditAmt(),
				 * bean.getOrgCreditLimit(), Integer.parseInt(xclDays[i]),
				 * userbean.getUserType(), userbean .getUserOrgId(),
				 * userbean.getUserId());
				 * 
				 * } else { map.put(retOrgId[i], agtBalDist); updHistXCL=false;
				 * }
				 * 
				 * } else if(Double.parseDouble(xclAmt[i]) == 0 &&
				 * Integer.parseInt(xclDays[i]) == 0){
				 * 
				 * 
				 * } else if(Double.parseDouble(xclAmt[i]) == 0){
				 * map.put(retOrgId[i], "Please Select Valid XCL Amount");
				 * updHistXCL=false; } else if(Double.parseDouble(xclDays[i]) ==
				 * 0){ map.put(retOrgId[i], "Please Select Valid XCL Days");
				 * updHistXCL=false; }
				 * 
				 * //------
				 * 
				 * System.out.println("****updHist*****"+updHist+"***");
				 * System.out.println("****updStatus*****"+updStatus+"***");
				 * System.out.println("****updHistXCL*****"+updHistXCL+"***");
				 * 
				 * if (updHist) { String msg = ""; if(updStatus){ msg =
				 * "Organization made "+orgStatus[i]+" by "+
				 * userbean.getRoleName().split(" ")[0]
				 * +": "+userbean.getOrgName(); stmtHistory.addBatch(
				 * "insert into st_lms_organization_master_history select " +
				 * userbean.getUserId() +
				 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'"
				 * + orgStatus[i] +"_MANUAL_"+
				 * userbean.getRoleName().split(" ")[0] +"','"+ msg
				 * +"', organization_status,'"+ new java.sql.Timestamp(new
				 * java.util.Date().getTime()).toString()+
				 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
				 * + retOrgId[i]);
				 * 
				 * System.out.println(
				 * "insert into st_lms_organization_master_history select " +
				 * userbean.getUserId() +
				 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'"
				 * + orgStatus[i] +"_MANUAL_"+
				 * userbean.getRoleName().split(" ")[0] +"','"+ msg
				 * +"', organization_status,'"+ new java.sql.Timestamp(new
				 * java.util.Date().getTime()).toString()+
				 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
				 * + retOrgId[i]); } else{ msg = "credit "+ balance[i];
				 * stmtHistory.addBatch(
				 * "insert into st_lms_organization_master_history select " +
				 * userbean.getUserId() +
				 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'CREDIT_CHANGED_"
				 * + userbean.getRoleName().split(" ")[0] +"','"+ msg
				 * +"', organization_status,'"+ new java.sql.Timestamp(new
				 * java.util.Date().getTime()).toString()+
				 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
				 * + retOrgId[i]);System.out.println(
				 * "insert into st_lms_organization_master_history select " +
				 * userbean.getUserId() +
				 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'CREDIT_CHANGED_"
				 * + userbean.getRoleName().split(" ")[0] +"','"+ msg
				 * +"', organization_status,'"+ new java.sql.Timestamp(new
				 * java.util.Date().getTime()).toString()+
				 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
				 * + retOrgId[i]); } }
				 * 
				 * updHistXCL = false; // Hard-Coding to be removed if
				 * (updHistXCL) { String msg = ""; if(updStatus){ msg =
				 * "Organization made "+orgStatus[i]+" by "+
				 * userbean.getRoleName().split(" ")[0]
				 * +": "+userbean.getOrgName(); stmtHistory.addBatch(
				 * "insert into st_lms_organization_master_history select " +
				 * userbean.getUserId() +
				 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'"
				 * + orgStatus[i] +"_MANUAL_"+
				 * userbean.getRoleName().split(" ")[0] +"','"+ msg
				 * +"', organization_status,'"+ new java.sql.Timestamp(new
				 * java.util.Date().getTime()).toString()+
				 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
				 * + retOrgId[i]);
				 * 
				 * System.out.println(
				 * "insert into st_lms_organization_master_history select " +
				 * userbean.getUserId() +
				 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'"
				 * + orgStatus[i] +"_MANUAL_"+
				 * userbean.getRoleName().split(" ")[0] +"','"+ msg
				 * +"', organization_status,'"+ new java.sql.Timestamp(new
				 * java.util.Date().getTime()).toString()+
				 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
				 * + retOrgId[i]); } else{ msg = "credit "+ balance[i];
				 * stmtHistory.addBatch(
				 * "insert into st_lms_organization_master_history select " +
				 * userbean.getUserId() +
				 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'CREDIT_CHANGED_"
				 * + userbean.getRoleName().split(" ")[0] +"','"+ msg
				 * +"', organization_status,'"+ new java.sql.Timestamp(new
				 * java.util.Date().getTime()).toString()+
				 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
				 * + retOrgId[i]);System.out.println(
				 * "insert into st_lms_organization_master_history select " +
				 * userbean.getUserId() +
				 * ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'CREDIT_CHANGED_"
				 * + userbean.getRoleName().split(" ")[0] +"','"+ msg
				 * +"', organization_status,'"+ new java.sql.Timestamp(new
				 * java.util.Date().getTime()).toString()+
				 * "',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
				 * + retOrgId[i]); }
				 * 
				 * }
				 * 
				 * } else { map.put(retOrgId[i],
				 * "Offline Retailer is currently LoggedIn"); } }
				 * stmt.executeBatch(); stmtHistory.executeBatch();
				 */
				con.commit();
			}
			dashBoardList = fetchMenuData();
			Map<String, DashBoardBean> newMap = (LinkedHashMap<String, DashBoardBean>) dashBoardList
					.get(0);
			java.util.Iterator<Entry<String, String>> itr = map.entrySet()
					.iterator();

			while (itr.hasNext()) {
				Map.Entry<String, String> pair = itr.next();
				newMap.get(pair.getKey()).setErrorMsg(pair.getValue());
			}

			return dashBoardList;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

	}

	private int calculateExtendsCreditLimitUpto(java.sql.Date date) {
		if (date == null) {
			return 0;
		}
		long days = 0, hours = 0;

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
			hours = timeDiff / (1000 * 60 * 60);
		}
		// System.out.println(" dd days : "+days +" hours = "+hours);
		// System.out.println(date +", extendedDate = "+extendedDate.getTime()
		// +" ,today : "+today.getTime());

		return (int) days;
	}
}
