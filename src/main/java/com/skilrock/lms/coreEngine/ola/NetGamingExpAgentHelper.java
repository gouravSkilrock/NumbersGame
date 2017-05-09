package com.skilrock.lms.coreEngine.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.ManualRequest;
import com.skilrock.lms.beans.NetGamingExpenseBean;
import com.skilrock.lms.beans.OlaCommissionBean;
import com.skilrock.lms.beans.SalePwtReportsBean;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;

public class NetGamingExpAgentHelper {

	static Log logger = LogFactory.getLog(NetGamingExpAgentHelper.class);

	public Map<String, NetGamingExpenseBean> fetchMenuData(String netGamingExpType,
			String startDate, String endDate , int walletId) throws LMSException {
		Connection con = null;
		Map<String, NetGamingExpenseBean> map = new LinkedHashMap<String, NetGamingExpenseBean>();
		con = DBConnect.getConnection();
		
		NetGamingExpenseBean netGamingBean = null;

		try {
			double agtCommRate = 0.0;
			String agtCommQry="select aaa.parent_id,om.name,sum(net_gaming) total_play,sum(agt_comm_amt) agt_comm,refTransactionId,status,updated_date from(select retailer_org_id,parent_id,name,net_gaming,agt_comm_amt,refTransactionId,status,updated_date from st_ola_retailer_"+ netGamingExpType.toLowerCase()+"_commission_exp we inner join st_lms_organization_master om on " 
				             +	"we.retailer_org_id=om.organization_id where we.wallet_id=" + walletId	+ " and we.date >= date(?) and we.date <= date(?) )aaa inner join st_lms_organization_master om on aaa.parent_id=om.organization_id group by aaa.parent_id";
			/*String childDataQry = "select b.id,a.name,b.agent_org_id,b.net_gaming,b.comm_rate,b.comm_amt,b.credit_note_number,b.status,b.updated_date from st_lms_organization_master a, st_ola_agent_"
					+ netGamingExpType.toLowerCase()
					+ "_commission_exp b where b.agent_org_id=a.organization_id and b.wallet_id=" + walletId
					+ " and b.date >= '"
					+ startDate + "' and b.date <= '" + endDate + "'";*/
			PreparedStatement pstmt = con.prepareStatement(agtCommQry);
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			System.out.println("query fecthing data net gaming:::" + pstmt);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				netGamingBean = new NetGamingExpenseBean();
				//netGamingBean.setTaskId(1);
				netGamingBean.setOrgId(rs.getInt("parent_id"));
				netGamingBean.setOrgName(rs.getString("name"));
				netGamingBean.setStatus(rs.getString("status"));
				netGamingBean.setNetGamingAmt(rs.getDouble("total_play"));
				netGamingBean.setNetGamingCommissionAmt(rs.getDouble("agt_comm"));
			
				
				//netGamingBean.setCrNote(rs.getString("credit_note_number"));
				String tempDate = rs.getString("updated_date");
				if (tempDate != null) {
					tempDate = tempDate.substring(0, tempDate.lastIndexOf('.'));
				}
				netGamingBean.setUpdateDate(tempDate);
				map.put(rs.getString("parent_id"), netGamingBean);
			}
		} catch (Exception e) {
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

		return map;
	}

	public String updateAgentData(int orgId[], UserInfoBean userBean,
			String netGamingExpType, String fromDate, String toDate,int walletId)
			throws LMSException {
		StringBuilder netGamingExpResp = new StringBuilder();
		Connection con = DBConnect.getConnection();
		Statement statement = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder orgIds = new StringBuilder();
		for (int i = 0; i < orgId.length; i++) {
			orgIds.append(orgId[i] + ",");
		}
		orgIds.deleteCharAt(orgIds.length() - 1);
		Timestamp updateTime = new Timestamp(new Date().getTime());
		try {
			con.setAutoCommit(false);
			statement = con.createStatement();
			String selectQry = "select id,sum(net_gaming) plr_net_gaming,sum(comm_amt) commission_calculated,agent_org_id,boUserId,boOrgId from ((select id,sum(net_gaming) net_gaming,sum(comm_amt) comm_amt,agent_org_id from st_ola_agent_"+netGamingExpType.toLowerCase()+"_commission_exp where status!='DONE' and date>='"+ fromDate+ "' and  date<='"+ toDate+ "' and wallet_id="+walletId+" and agent_org_id in (" + orgIds + ") group by agent_org_id)netGaming inner join(select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT')orgDetails on netGaming.agent_org_id=orgDetails.agtOrgId) group by agent_org_id";
			ResultSet rs = statement.executeQuery(selectQry);
			while (rs.next()) {
				OlaCommissionHelper.updateAgentBackOfficeDetails(con, rs.getInt("agent_org_id"), walletId,rs.getDouble("plr_net_gaming"),rs.getDouble("commission_calculated"),df.parse(fromDate), df.parse(toDate), rs.getInt("boUserId"),rs.getInt("boOrgId"),"MANUAL",netGamingExpType,rs.getInt("id"));
				/*PreparedStatement statement2 = con.prepareStatement("select plr_net_gaming,commission_calculated,ret_org_id from(select sum(plr_net_gaming) plr_net_gaming,sum(commission_calculated) commission_calculated,ret_org_id from st_ola_daily_retailer_commission_"+walletId+"  where status='PENDING' and date>='"+fromDate+"' and date<='"+toDate+"' group by ret_org_id)a inner join (select um.organization_id retalerOrgId,om.parent_id agentOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.parent_id="+rs.getInt("agentOrgId")+")b on a.ret_org_id=b.retalerOrgId");
				ResultSet rs1 = statement2.executeQuery();
				while(rs1.next())
				{
					OlaCommissionHelper.updateRetailerAgentDetails(con, rs1.getInt("ret_org_id"), walletId,rs1.getDouble("plr_net_gaming"),rs1.getDouble("commission_calculated"),df.parse(fromDate), df.parse(toDate), "MANUAL");
				}
				updateNetGamingExpAgentData(updateTime, fromDate, toDate, rs.getInt("id"), rs.getInt("agent_org_id"), rs.getInt("net_gaming"), userBean, "MANUAL", netGamingExpType, con);
			*/}

			selectQry = "select agent_org_id,status,credit_note_number,updated_date from st_ola_agent_"
					+ netGamingExpType.toLowerCase()
					+ "_commission_exp where agent_org_id in ("
					+ orgIds + ")";
			rs = statement.executeQuery(selectQry);
			while (rs.next()) {
				netGamingExpResp.append(rs.getInt("agent_org_id")
						+ ","
						+ rs.getString("status")
						+ ","
						+ rs.getString("credit_note_number")
						+ ","
						+ rs.getString("updated_date").substring(0,
								rs.getString("updated_date").lastIndexOf('.'))
						+ "Nxt");
			}
			con.commit();
			DBConnect.closeCon(con);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(netGamingExpResp);
		return netGamingExpResp.toString();
	}

	public void updateAllAgentData(String fromDate, String toDate,
			String netGamingExpType, UserInfoBean userBean, int walletId) {/*
		Connection con = DBConnect.getConnection();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			con.setAutoCommit(false);
			OlaCommissionBean olaCommissionBean = null;
			
			Map<Integer, OlaCommissionBean> commDetailMapForAgent = OlaCommissionHelper.getCommissionDetailAgentWise(
					con, walletId, df.parse(startDate), df.parse(endDate));
			Set<Integer> agentOrgSet = commDetailMapForAgent.keySet();
			for (int agentOrgId : agentOrgSet) {
				olaCommissionBean = commDetailMapForAgent.get(agentOrgId);
				OlaCommissionHelper.updateAgentBackOfficeDetails(con, agentOrgId, walletId,
						olaCommissionBean.getTotalPlayerNetGaming(),
						olaCommissionBean.getTotalCommissionCalculated(),
						df.parse(startDate), df.parse(endDate), olaCommissionBean.getBoUserId(),
						olaCommissionBean.getBoUserOrgId(),"MANUAL");
			}
			Map<Integer, OlaCommissionBean> commDetailMap = OlaCommissionHelper.getCommissionDetailRetailerWise(
					con, walletId, df.parse(startDate), df.parse(endDate));
			Set<Integer> retOrgSet = commDetailMap.keySet();
			for (int retOrgId : retOrgSet) {
				olaCommissionBean = commDetailMap.get(retOrgId);
				OlaCommissionHelper.updateRetailerAgentDetails(con, retOrgId, walletId,
						olaCommissionBean.getTotalPlayerNetGaming(),
						olaCommissionBean.getTotalCommissionCalculated(),
						df.parse(startDate), df.parse(endDate), "MANUAL");
			}
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		Connection con = DBConnect.getConnection();
		Statement statement = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp updTime = new Timestamp(new Date().getTime());
		try {
			con.setAutoCommit(false);
			statement = con.createStatement();
			String selectQry = "select id,sum(net_gaming) plr_net_gaming,sum(comm_amt) commission_calculated,agent_org_id,boUserId,boOrgId from ((select id,sum(net_gaming) net_gaming,sum(comm_amt) comm_amt,agent_org_id from st_ola_agent_"+netGamingExpType.toLowerCase()+"_commission_exp where status!='DONE' and date>='"+ fromDate+ "' and  date<='"+ toDate+ "' and wallet_id="+walletId+" group by agent_org_id)netGaming inner join(select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT')orgDetails on netGaming.agent_org_id=orgDetails.agtOrgId) group by agent_org_id";
			ResultSet rs = statement.executeQuery(selectQry);
			while (rs.next()) {
				OlaCommissionHelper.updateAgentBackOfficeDetails(con, rs.getInt("agent_org_id"), walletId,rs.getDouble("plr_net_gaming"),rs.getDouble("commission_calculated"),df.parse(fromDate), df.parse(toDate), rs.getInt("boUserId"),rs.getInt("boOrgId"),"MANUAL",netGamingExpType,rs.getInt("id"));
			}
			con.commit();
			DBConnect.closeCon(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}