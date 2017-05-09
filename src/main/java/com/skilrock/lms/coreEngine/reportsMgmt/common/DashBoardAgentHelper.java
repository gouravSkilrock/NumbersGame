package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.DashBoardBean;
import com.skilrock.lms.beans.HistoryBean;
import com.skilrock.lms.beans.LimitDistributionBean;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.userMgmt.common.AgentManagementHelper;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.PaymentLedgerRetailerWiseReportAction;
public class DashBoardAgentHelper {

	static Log logger = LogFactory.getLog(DashBoardAgentHelper.class);
	 private Timestamp startDate = null;
	 private Timestamp endDate = null;
	 private Timestamp deployDate = null;
 
	public static void main(String[] args) throws LMSException {
	//	new DashBoardAgentHelper().fetchMenuData(4);
	}
	public List<Object> fetchMenuDataDaysLimit(int agentOrgId) throws LMSException {
		Connection con = null;
		RetailerDetailedCollectionReportHelper helper = null;
		Map<String, CollectionReportOverAllBean> retailerMap = null;
		Map<String, CollectionReportOverAllBean> lowRetailerMap = null;
		List<Object> dashBoardList = new ArrayList<Object>();
		List<Integer> lowRetList=new ArrayList<Integer>();
		Map<String, DashBoardBean> map = new LinkedHashMap<String, DashBoardBean>();
		con = DBConnect.getConnection();
		LimitDistributionBean limitBean = new LimitDistributionBean();
		dashBoardList.add(limitBean);
		dashBoardList.add(map);
		DashBoardBean tempBean = null;
		String orgCodeQry = "  name orgCode ";
			
		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = " org_code orgCode ";

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(org_code,'_',name)  orgCode ";
		
		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = " concat(name,'_',org_code)  orgCode ";
		
		}
		String childDataQry = "select organization_id,"+orgCodeQry+",city,organization_status,(available_credit-claimable_bal) as balance from st_lms_organization_master where parent_id="
				+ agentOrgId + " AND organization_status<>'TERMINATE' order by "+QueryManager.getAppendOrgOrder();
		try {
		ServletContext sc=LMSUtility.sc;	
			String deploy_date=Utility.getPropertyValue("DEPLOYMENT_DATE");
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date toDate = Calendar.getInstance().getTime();
			String end_date=dateFormat.format(toDate);
			String start_date=end_date;
			dateChanger(start_date, end_date, deploy_date);
			helper = new RetailerDetailedCollectionReportHelper();
			retailerMap = helper.getRetailerWiseClosingBalance(deployDate, startDate, endDate, agentOrgId);
			PreparedStatement pstmt = con.prepareStatement(childDataQry);
			ResultSet rs = pstmt.executeQuery();
			double retOpenBal=0;
			while (rs.next()) {
				tempBean = new DashBoardBean();
				tempBean.setOrgId(rs.getInt("organization_id"));
				if(retailerMap.get(rs.getString("organization_id")) != null)
				retOpenBal=retailerMap.get(rs.getString("organization_id")).getClosingBalance();
				tempBean.setClosingBalance(CommonMethods.fmtToTwoDecimal(retOpenBal));
				if(retOpenBal>0){
					lowRetList.add(rs.getInt("organization_id"));
				}else{
					tempBean.setNoOfDays("0");
				}
				tempBean.setOrgName(rs.getString("orgCode"));
				tempBean.setOrgStatus(rs.getString("organization_status"));
				tempBean.setLocation(rs.getString("city"));
				tempBean.setBalance(rs.getString("balance"));
				map.put(rs.getInt("organization_id") + "", tempBean);
			}
		//	lowRetailerMap=helper.getLowRetailerClosingBalance(deployDate, startDate, endDate, agentOrgId, lowRetList);
			if(lowRetList.size()>0)
				lowRetailerMap=helper.getLowRetailerClosingBalanceWithAnyDayLimit(deployDate, startDate, endDate, agentOrgId);
			
			for(int retlOrgId:lowRetList){
				if(lowRetailerMap.get(String.valueOf(retlOrgId)).getNoOfDays()!=null)
				map.get(String.valueOf(retlOrgId)).setNoOfDays(lowRetailerMap.get(String.valueOf(retlOrgId)).getNoOfDays());	
			}
			
			

			fetchParentBalance(con, rs, pstmt, limitBean, agentOrgId);
			String retloginStatusQry = "select organization_id,offline_status,is_offline,serial_number,device_type from st_lms_ret_offline_master where organization_id in (select organization_id from st_lms_organization_master where parent_id="
					+ agentOrgId + " AND organization_status<>'TERMINATE')";
			pstmt = con.prepareStatement(retloginStatusQry);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tempBean = map.get(rs.getString("organization_id"));
				tempBean.setDeviceName(rs.getString("device_type")+"-"+rs.getString("serial_number"));
				tempBean.setWebType(rs.getString("is_offline"));
				tempBean.setOfflineStatus(rs.getString("offline_status"));
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} 
		catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}catch (Exception ex) {
			ex.printStackTrace();
		}finally {
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
		System.out.println(map);
		return dashBoardList;
	}
	
	public List<Object> fetchMenuData(int agentOrgId) throws LMSException {
		Connection con = null;
		List<Object> dashBoardList = new ArrayList<Object>();
		Map<String, DashBoardBean> map = new LinkedHashMap<String, DashBoardBean>();
		con = DBConnect.getConnection();
		LimitDistributionBean limitBean = new LimitDistributionBean();
		dashBoardList.add(limitBean);
		dashBoardList.add(map);
		DashBoardBean tempBean = null;
		String orgCodeQry = "  name orgCode ";
		
		
		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = " org_code orgCode ";


		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(org_code,'_',name)  orgCode ";
		

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = " concat(name,'_',org_code)  orgCode ";
		

		}
		String childDataQry = "select organization_id,"+orgCodeQry+",city,organization_status,(available_credit-claimable_bal) as balance from st_lms_organization_master where parent_id="
				+ agentOrgId + " AND organization_status<>'TERMINATE' order by "+QueryManager.getAppendOrgOrder();
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
				map.put(rs.getInt("organization_id") + "", tempBean);
			}
			fetchParentBalance(con, rs, pstmt, limitBean, agentOrgId);
			String retloginStatusQry = "select organization_id,offline_status,is_offline,serial_number,device_type from st_lms_ret_offline_master where organization_id in (select organization_id from st_lms_organization_master where parent_id="
					+ agentOrgId + " AND organization_status<>'TERMINATE')";
			pstmt = con.prepareStatement(retloginStatusQry);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tempBean = map.get(rs.getString("organization_id"));
				tempBean.setDeviceName(rs.getString("device_type")+"-"+rs.getString("serial_number"));
				tempBean.setWebType(rs.getString("is_offline"));
				tempBean.setOfflineStatus(rs.getString("offline_status"));
			}
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
		System.out.println(map);
		return dashBoardList;
	}
	
	private  void  dateChanger(String start_date, String end_date,String deploy_date){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try{
		startDate = new Timestamp(dateFormat
				.parse(start_date).getTime());
		endDate = new Timestamp(dateFormat
				.parse(end_date).getTime()+ 24 * 60 * 60 * 1000 - 1000);
		deployDate = new Timestamp(dateFormat
				.parse(deploy_date).getTime());
		}catch(ParseException ex){
			ex.printStackTrace();
		}
	}

	private void fetchParentBalance(Connection con, ResultSet rs,
			PreparedStatement pstmt, LimitDistributionBean limitBean,
			int agentOrgId) throws SQLException {
		String parentBalQry = "select organization_id,name, credit_limit, extended_credit_limit, live_balance, distributed ,ifnull((live_balance - distributed),0) as distributable from (select organization_id, name, organization_type, credit_limit, extended_credit_limit,(available_credit-claimable_bal)as live_balance from st_lms_organization_master where organization_type='AGENT' and organization_id="
				+ agentOrgId
				+ ")main, (select parent_id, ifnull(sum(if((available_credit-claimable_bal)>0,(available_credit-claimable_bal),0)),0) as distributed from st_lms_organization_master where organization_type = 'RETAILER' and parent_id ="
				+ agentOrgId
				+ " group by parent_id)sub where main.organization_id = sub.parent_id";

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

	public List<Object> updateAgentData(int agentOrgId, String[] retOrgId,
			String[] balance, String[] orgStatus, UserInfoBean userbean, String requestIp)
			throws LMSException {
		Connection con = null;
		List<Object> dashBoardList = null;
		Map<String, String> map = new LinkedHashMap<String, String>();
		con = DBConnect.getConnection();
		PreparedStatement getOrgPstmt=null;
		ResultSet getOrgRs=null;
		OrganizationBean orgBean = null;
        PreparedStatement updateOrgPstmt=null;
        PreparedStatement insertHistoryPstmt=null;
		
		try {
			AgentManagementHelper editOrgDetail = new AgentManagementHelper();
			
			for (int i = 0; i < retOrgId.length; i++) {
				con.setAutoCommit(false);
				
				getOrgPstmt=con.prepareStatement("select organization_id,parent_id,organization_type,name,available_credit,claimable_bal,unclaimable_bal,credit_limit,security_deposit,organization_status,extended_credit_limit,current_credit_amt,extends_credit_limit_upto from st_lms_organization_master where organization_id=?");
				getOrgPstmt.setInt(1, Integer
						.parseInt(retOrgId[i]));
				getOrgRs=getOrgPstmt.executeQuery();
				if(getOrgRs.next()){
					orgBean = new OrganizationBean();
					orgBean.setOrgId(getOrgRs.getInt("organization_id"));
					orgBean.setParentOrgId(getOrgRs.getInt("parent_id"));
					orgBean.setOrgName(getOrgRs.getString(TableConstants.ORG_NAME));
					orgBean.setOrgType(getOrgRs.getString(TableConstants.ORG_TYPE));
					orgBean.setSecDeposit(getOrgRs.getDouble("security_deposit"));
					orgBean.setOrgCreditLimit(getOrgRs
							.getDouble(TableConstants.CREDIT_LIMIT));
					orgBean.setOrgStatus(getOrgRs.getString(TableConstants.ORG_STATUS));
					orgBean.setCurrentCreditAmt(getOrgRs
							.getDouble(TableConstants.SOM_CURR_CREDIT_AMT));
					orgBean.setExtendedCredit(getOrgRs
							.getDouble(TableConstants.EXTENDED_CREDIT_LIMIT));
					orgBean.setAvailableCredit(getOrgRs
							.getDouble(TableConstants.SOM_AVAILABLE_CREDIT));
					orgBean.setClaimableBal(getOrgRs.getDouble("claimable_bal"));
					
						
				}
				
				getOrgPstmt=con.prepareStatement("select login_status from st_lms_ret_offline_master where organization_id= ? and is_offline='YES'");
				getOrgPstmt.setInt(1, Integer
						.parseInt(retOrgId[i]));
				getOrgRs=getOrgPstmt.executeQuery();
				if(getOrgRs.next()){
					map.put(retOrgId[i],
					"Offline Retailer is currently LoggedIn");
					continue;
				} else {
				
					if (!orgBean.getOrgStatus().equals(orgStatus[i])) {
						
						if (orgStatus[i].equalsIgnoreCase("ACTIVE")) {
							if ((orgBean.getAvailableCredit()
									- orgBean.getClaimableBal() + Double
									.parseDouble(balance[i])) < 0) {
								map.put(retOrgId[i],
												"Status Can't be made ACTIVE on -ve balance");
								continue;
							}
						}

						String prevOrgStatus = "";
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT organization_status FROM st_lms_organization_master WHERE organization_id="+Integer.parseInt(retOrgId[i])+";");
						if(rs.next()) {
							prevOrgStatus = rs.getString("organization_status");
						}

						HistoryBean historyBean = new HistoryBean(Integer.parseInt(retOrgId[i]), userbean.getUserId(), "UPDATED FROM DASHBOARD", requestIp);
						if(!prevOrgStatus.equalsIgnoreCase(orgStatus[i].trim())) {
							historyBean.setChangeType("ORGANIZATION_STATUS");
							historyBean.setChangeValue(prevOrgStatus);
							historyBean.setUpdatedValue(orgStatus[i]);
							CommonMethods.insertUpdateOrganizationHistory(historyBean, con);
						}

						/*
						updateOrgPstmt=con.prepareStatement("update st_lms_organization_master set organization_status=? where organization_id=?");
						updateOrgPstmt.setString(1, orgStatus[i]);
						updateOrgPstmt.setInt(2, Integer
								.parseInt(retOrgId[i]));
						
						updateOrgPstmt.executeUpdate();
						
						
						insertHistoryPstmt=con.prepareStatement("insert into st_lms_organization_master_history select ?,organization_id, addr_line1, addr_line2,division_code,area_code, city, pin_code, security_deposit, credit_limit,?,?,organization_status,?, pwt_scrap, recon_report_type  from st_lms_organization_master where organization_id =?");
						insertHistoryPstmt.setInt(1, userbean.getUserId());
						insertHistoryPstmt.setString(2, orgStatus[i] +"_MANUAL_"+ userbean.getRoleName().split(" ")[0]);
						insertHistoryPstmt.setString(3, "Organization made "+orgStatus[i]+" by "+ userbean.getRoleName().split(" ")[0] +": "+userbean.getOrgName());
						insertHistoryPstmt.setTimestamp(4, Util.getCurrentTimeStamp());
						insertHistoryPstmt.setInt(5, Integer
								.parseInt(retOrgId[i]));
						insertHistoryPstmt.executeUpdate();
						*/
					}
			

					if (Double.parseDouble(balance[i]) != 0) {
						
						String agtBalDist = "TRUE";
						agtBalDist = CommonMethods.changeCreditLimitRet(Integer
								.parseInt(retOrgId[i]), orgBean
								.getOrgCreditLimit()
								+ Double.parseDouble(balance[i]), "CL");
						System.out.println(agtBalDist + "******" + retOrgId[i]);
						if (agtBalDist.equals("TRUE")) {
							editOrgDetail.editOrgCreditLimitDetails(orgBean
									, Double.parseDouble(balance[i]), userbean,con);

						} else {
							map.put(retOrgId[i], agtBalDist);
							
						}
					}
					}
				
				con.commit();
			}
			
			
			dashBoardList = fetchMenuData(agentOrgId);
			Map<String, DashBoardBean> newMap = (LinkedHashMap<String, DashBoardBean>) dashBoardList
					.get(1);
			java.util.Iterator<Entry<String, String>> itr = map.entrySet()
					.iterator();

			while (itr.hasNext()) {
				Map.Entry<String, String> pair = itr.next();
				newMap.get(pair.getKey()).setErrorMsg(pair.getValue());
			}

			return dashBoardList;
		
		
		
		/*
		 * 
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			Statement stmtHistory = con.createStatement();
			for (int i = 0; i < retOrgId.length; i++) {
				AgentManagementHelper editOrgDetail = new AgentManagementHelper();
				OrganizationBean bean = editOrgDetail.orgDetails(Integer
						.parseInt(retOrgId[i]));
				
				if (!DrawGameOfflineHelper.fetchLoginStatus(Integer
						.parseInt(retOrgId[i]))) {

					if (!bean.getOrgStatus().equals(orgStatus[i])) {
						
						if (orgStatus[i].equalsIgnoreCase("ACTIVE")) {
							if ((bean.getAvailableCredit()
									- bean.getClaimableBal() + Double
									.parseDouble(balance[i])) < 0) {
								updHist = false;
								map
										.put(retOrgId[i],
												"Status Can't be made ACTIVE on -ve balance");
							}
						}
						
							stmt
									.addBatch("update st_lms_organization_master set organization_status='"
											+ orgStatus[i]
											+ "' where organization_id="
											+ retOrgId[i] + "");
						

						String msg = "";
						
							msg = "Organization made "+orgStatus[i]+" by "+ userbean.getRoleName().split(" ")[0] +": "+userbean.getOrgName();
							stmtHistory
							.addBatch("insert into st_lms_organization_master_history select "
									+ userbean.getUserId()
									+ ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'"+ orgStatus[i] +"_MANUAL_"+ userbean.getRoleName().split(" ")[0] +"','"+ msg +"', organization_status,'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id="
									+ retOrgId[i]);
							
							System.out.println("insert into st_lms_organization_master_history select " + userbean.getUserId() 	+ ", organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit,'"+ orgStatus[i] +"_MANUAL_"+ userbean.getRoleName().split(" ")[0] +"','"+ msg +"', organization_status,'"+ new java.sql.Timestamp(new java.util.Date().getTime()).toString() +"',pwt_scrap,recon_report_type from st_lms_organization_master slom where organization_id=" + retOrgId[i]);
						
					

					
						
					}
					if (Double.parseDouble(balance[i]) != 0) {
						
						String agtBalDist = "TRUE";
						agtBalDist = CommonMethods.changeCreditLimitRet(Integer
								.parseInt(retOrgId[i]), bean
								.getOrgCreditLimit()
								+ Double.parseDouble(balance[i]), "CL");
						System.out.println(agtBalDist + "******" + retOrgId[i]);
						if (agtBalDist.equals("TRUE")) {
							editOrgDetail.editOrgCreditLimitDetails(bean
									, Double.parseDouble(balance[i]), userbean,con);

						} else {
							map.put(retOrgId[i], agtBalDist);
							
						}
					}
					
					System.out.println("****updHist*****"+updHist+"***");
					System.out.println("****updStatus*****"+updStatus+"***");
				

				} else {
					map.put(retOrgId[i],
							"Offline Retailer is currently LoggedIn");
				}
			}
			stmt.executeBatch();
			stmtHistory.executeBatch();
			con.commit();
			dashBoardList = fetchMenuData(agentOrgId);
			Map<String, DashBoardBean> newMap = (LinkedHashMap<String, DashBoardBean>) dashBoardList
					.get(1);
			java.util.Iterator<Entry<String, String>> itr = map.entrySet()
					.iterator();

			while (itr.hasNext()) {
				Map.Entry<String, String> pair = itr.next();
				newMap.get(pair.getKey()).setErrorMsg(pair.getValue());
			}

			return dashBoardList;
		
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

	}

}