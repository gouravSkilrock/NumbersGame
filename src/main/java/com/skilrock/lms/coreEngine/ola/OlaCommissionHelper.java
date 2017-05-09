package com.skilrock.lms.coreEngine.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.ManualRequest;
import com.skilrock.lms.beans.OlaCommissionBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class OlaCommissionHelper  {

	public void execute() {

		int walletId = 2;
		Calendar calStart = Calendar.getInstance();
		java.sql.Date date = new java.sql.Date(calStart.getTime().getTime());
		//updateRetailerCommissionDetail(date, date, walletId,"AUTO");
	}

	public void updateRetailerCommissionDetail(Date startDate,
			Date endDate ,String updateMode , String updateType) {
		
		Connection con = DBConnect.getConnection();
		
		
		try {
			con.setAutoCommit(false);
			String walletQry = "select wallet_id, wallet_name from st_ola_wallet_master";
			PreparedStatement walletPstmt = con.prepareStatement(walletQry);
			ResultSet rsWallet = walletPstmt.executeQuery();
			while (rsWallet.next()) {
				int walletId = rsWallet.getInt("wallet_id");
			if (updateType.equalsIgnoreCase("WEEKLY")) {
				
				OlaCommissionBean olaCommissionBean = null;
				Map<Integer, OlaCommissionBean> commDetailMapForAgent = getCommWeeklyWiseForAgent(
						con, walletId, startDate, endDate);
				Set<Integer> agentOrgSet = commDetailMapForAgent.keySet();
				for (int agentOrgId : agentOrgSet) {
					olaCommissionBean = commDetailMapForAgent.get(agentOrgId);
					if(olaCommissionBean.getTotalPlayerNetGaming()>0)
					{
					updateAgentBackOfficeDetails(con, agentOrgId, walletId,
							olaCommissionBean.getTotalPlayerNetGaming(),
							olaCommissionBean.getTotalCommissionCalculated(),
							startDate, endDate,
							olaCommissionBean.getBoUserId(), olaCommissionBean
									.getBoUserOrgId(), updateMode,updateType,olaCommissionBean.getId());
					}
					else
					{
						PreparedStatement statement = con.prepareStatement("update st_ola_agent_weekly_commission_exp set status='NOT_PROCESSED' where id="+olaCommissionBean.getId()+"");
						statement.executeUpdate();
					}
					
				}
				Map<Integer, OlaCommissionBean> commDetailMap = getCommWeeklyWiseForRetailer(
						con, walletId, startDate, endDate);
				Set<Integer> retOrgSet = commDetailMap.keySet();
				for (int retOrgId : retOrgSet) {
					olaCommissionBean = commDetailMap.get(retOrgId);
//					if(olaCommissionBean.getPlayerNetGaming()>0)
					if (olaCommissionBean.getTotalPlayerNetGaming()>0) // by neeraj 
					{
					updateRetailerAgentDetails(con, retOrgId, walletId,
							olaCommissionBean.getTotalPlayerNetGaming(),
							olaCommissionBean.getTotalCommissionCalculated(),
							startDate, endDate, updateMode,updateType,olaCommissionBean.getId());
					}
					else
					{
						PreparedStatement statement1 = con.prepareStatement("update st_ola_retailer_weekly_commission_exp set status='NOT_PROCESSED' where id="+olaCommissionBean.getId()+"");
						statement1.executeUpdate();
					}
				}
				System.out.println("NetGaming Data Weekly Wise Updated Successfully");
				con.commit();

			} else if (updateType.equalsIgnoreCase("MONTHLY")) {
				con.setAutoCommit(false);
				OlaCommissionBean olaCommissionBean = null;
				Map<Integer, OlaCommissionBean> commDetailMapForAgent = getCommMonthlyWiseForAgent(
						con, walletId, startDate, endDate);
				Set<Integer> agentOrgSet = commDetailMapForAgent.keySet();
				for (int agentOrgId : agentOrgSet) {
					olaCommissionBean = commDetailMapForAgent.get(agentOrgId);
//					if(olaCommissionBean.getPlayerNetGaming()>0)
					if (olaCommissionBean.getTotalPlayerNetGaming()>0) // by neeraj 
					{
					updateAgentBackOfficeDetails(con, agentOrgId, walletId,
							olaCommissionBean.getTotalPlayerNetGaming(),
							olaCommissionBean.getTotalCommissionCalculated(),
							startDate, endDate,
							olaCommissionBean.getBoUserId(), olaCommissionBean
									.getBoUserOrgId(), updateMode ,updateType,olaCommissionBean.getId());
					}
					else
					{
						PreparedStatement statement3 = con.prepareStatement("update st_ola_agent_monthly_commission_exp set status='NOT_PROCESSED' where id="+olaCommissionBean.getId()+"");
						statement3.executeUpdate();
					}
				}
				Map<Integer, OlaCommissionBean> commDetailMap = getCommMonthlyWiseForRetailer(
						con, walletId, startDate, endDate);
				Set<Integer> retOrgSet = commDetailMap.keySet();
				for (int retOrgId : retOrgSet) {
					olaCommissionBean = commDetailMap.get(retOrgId);
							//			if(olaCommissionBean.getPlayerNetGaming()>0)
					if (olaCommissionBean.getTotalPlayerNetGaming()>0) // by neeraj 
					{
					
					updateRetailerAgentDetails(con, retOrgId, walletId,
							olaCommissionBean.getTotalPlayerNetGaming(),
							olaCommissionBean.getTotalCommissionCalculated(),
							startDate, endDate, updateMode,updateType,olaCommissionBean.getId());
					}
					else
					{
						PreparedStatement statement4 = con.prepareStatement("update st_ola_retailer_monthly_commission_exp set status='NOT_PROCESSED' where id="+olaCommissionBean.getId()+"");
						statement4.executeUpdate();
					}
				}
				System.out.println("NetGaming Data Monthly Wise Updated Successfully");
				

			}
		}
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public static void updateRetailerAgentDetails(Connection con, int retOrgId,
			int walletId, double plrNetGaming, double commissionCalculated,
			Date startDate, Date endDate, String updateMode,String updateType,int generatedId) throws LMSException
	{
		int isUpdate = 0;
		int id;
		double agentCommission;
		try {
			UserInfoBean userBean = CommonFunctionsHelper.getAgentDetails(retOrgId, con);
			double agtCommRate = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, userBean.getUserOrgId(), "NETGAMING", "AGENT",
					con);
			agentCommission = (plrNetGaming * agtCommRate * .01);
			String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('AGENT','OLA','WEB')";
			PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
			long transactionId = 0;
			pstmt1.executeUpdate();
			ResultSet rs1 = pstmt1.getGeneratedKeys();
			if (rs1.next()) {
				transactionId = rs1.getLong(1);
				pstmt1 = con
						.prepareStatement("INSERT INTO st_lms_agent_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
				pstmt1.setLong(1, transactionId);
				pstmt1.setInt(2, userBean.getUserId());
				pstmt1.setInt(3, userBean.getUserOrgId());
				pstmt1.setString(4, "RETAILER");
				pstmt1.setInt(5, retOrgId);
				pstmt1.setString(6, "OLA_COMMISSION");
				pstmt1.setTimestamp(7, new java.sql.Timestamp(new Date()
						.getTime()));

				pstmt1.executeUpdate();
			}

			pstmt1 = con
					.prepareStatement("insert into st_ola_agt_ret_commission(agt_org_id,ret_org_id,wallet_id,plr_net_gaming,commission_calculated,agent_commission,agt_comm_rate,transaction_id,start_date,end_date,claim_status,update_mode) values (?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt1.setInt(1, userBean.getUserOrgId());
			pstmt1.setInt(2, retOrgId);
			pstmt1.setInt(3, walletId);
			pstmt1.setDouble(4, plrNetGaming);
			pstmt1.setDouble(5, commissionCalculated);
			pstmt1.setDouble(6, agentCommission);
			pstmt1.setDouble(7, agtCommRate);
			pstmt1.setLong(8, transactionId);
			pstmt1.setDate(9, new java.sql.Date(startDate.getTime()));
			pstmt1.setDate(10, new java.sql.Date(endDate.getTime()));
			pstmt1.setString(11, "DONE_CLAIM");
			pstmt1.setString(12, updateMode);
			isUpdate = pstmt1.executeUpdate();
			System.out.println("isUpdate" + isUpdate);
			if (isUpdate == 1 && updateType.equalsIgnoreCase("WEEKLY")) {
				pstmt1 = con.prepareStatement("update st_ola_retailer_weekly_commission_exp set status='DONE',refTransactionId = "+ transactionId+ ", updated_date='"+ new java.sql.Timestamp(new Date().getTime())+"' , updated_mode='AUTO' where id="+generatedId+"");
				pstmt1.executeUpdate();
			}
			else if(isUpdate == 1 && updateType.equalsIgnoreCase("MONTHLY"))
			{
				pstmt1 = con.prepareStatement("update st_ola_retailer_monthly_commission_exp set status='DONE',refTransactionId = "+ transactionId+ ", updated_date='"+ new java.sql.Timestamp(new Date().getTime())+"' , updated_mode='AUTO' where id="+generatedId+"");
				pstmt1.executeUpdate();
			}
			
			// update retailers available balance
			OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId,
					"OLA_COMMISSION", commissionCalculated, con);
		
			// insert into receipt master
			PreparedStatement preState5 = con.prepareStatement(QueryManager
					.getAGENTLatestReceiptNb());
			preState5.setString(1, "RECEIPT");
			preState5.setInt(2, userBean.getUserOrgId());
			ResultSet recieptRs = preState5.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
					"RECEIPT", lastRecieptNoGenerated, "AGENT", userBean
							.getUserOrgId());

			// insert into receipt master table
			PreparedStatement preState4 = con.prepareStatement(QueryManager
					.insertInReceiptMaster());
			preState4.setString(1, "AGENT");
			preState4.executeUpdate();

			ResultSet rs12 = preState4.getGeneratedKeys();
			rs12.next();
			id = rs12.getInt(1);

			// insert into agent reciept table
			preState4 = con.prepareStatement(QueryManager
					.insertInAgentReceipts());

			preState4.setInt(1, id);
			preState4.setString(2, "RECEIPT");
			preState4.setInt(3, userBean.getUserOrgId());
			preState4.setInt(4, retOrgId);
			preState4.setString(5, "RETAILER");
			preState4.setString(6, autoGeneRecieptNo);
			preState4.setTimestamp(7, Util.getCurrentTimeStamp());

			preState4.executeUpdate();
			PreparedStatement preState3 = con.prepareStatement(QueryManager
					.insertAgentReceiptTrnMapping());
			preState3.setInt(1, id);
			preState3.setLong(2, transactionId);
			preState3.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}

	public static Map<Integer, OlaCommissionBean> getCommWeeklyWiseForRetailer(
			Connection con, int walletId, Date startDate, Date endDate)
			throws SQLException {
		Map<Integer, OlaCommissionBean> olaMap = new TreeMap<Integer, OlaCommissionBean>();
		String commQuery = "select id,sum(net_gaming) plr_net_gaming,sum(comm_amt) commission_calculated,retailer_org_id from st_ola_retailer_weekly_commission_exp where status='PENDING' and date>='"+ startDate+ "' and  date<='" + endDate + "' and wallet_id="+walletId+" group by retailer_org_id";
		System.out.println(commQuery);
		PreparedStatement pstmt = con.prepareStatement(commQuery);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int retOrgId = rs.getInt("retailer_org_id");
			OlaCommissionBean olaCommissionBean = new OlaCommissionBean();
			olaCommissionBean.setTotalPlayerNetGaming(rs
					.getDouble("plr_net_gaming"));
			olaCommissionBean.setTotalCommissionCalculated(rs
					.getDouble("commission_calculated"));
			olaCommissionBean.setId(rs.getInt("id"));
			olaMap.put(retOrgId, olaCommissionBean);
		}
		return olaMap;
	}
	public static Map<Integer, OlaCommissionBean> getCommMonthlyWiseForRetailer(
			Connection con, int walletId, Date startDate, Date endDate)
			throws SQLException {
		Map<Integer, OlaCommissionBean> olaMap = new TreeMap<Integer, OlaCommissionBean>();
		String commQuery = "select id,sum(net_gaming) plr_net_gaming,sum(comm_amt) commission_calculated,retailer_org_id from st_ola_retailer_monthly_commission_exp where status='PENDING' and date>='"+ startDate+ "' and  date<='" + endDate + "' and wallet_id="+walletId+" group by retailer_org_id";
		System.out.println(commQuery);
		PreparedStatement pstmt = con.prepareStatement(commQuery);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int retOrgId = rs.getInt("retailer_org_id");
			OlaCommissionBean olaCommissionBean = new OlaCommissionBean();
			olaCommissionBean.setTotalPlayerNetGaming(rs
					.getDouble("plr_net_gaming"));
			olaCommissionBean.setTotalCommissionCalculated(rs
					.getDouble("commission_calculated"));
			olaCommissionBean.setId(rs.getInt("id"));
			olaMap.put(retOrgId, olaCommissionBean);
		}
		return olaMap;
	}

	public static Map<Integer, OlaCommissionBean> getCommWeeklyWiseForAgent(
			Connection con, int walletId, Date startDate, Date endDate)
			throws SQLException {
		Map<Integer, OlaCommissionBean> olaMap = new TreeMap<Integer, OlaCommissionBean>();
		String commQuery = "select id,sum(net_gaming) plr_net_gaming,sum(comm_amt) commission_calculated,agent_org_id,boUserId,boOrgId from ((select id,sum(net_gaming) net_gaming,sum(comm_amt) comm_amt,agent_org_id from st_ola_agent_weekly_commission_exp where status='PENDING' and date>='"+ startDate+ "' and  date<='"+ endDate+ "' and wallet_id="+walletId+" group by agent_org_id)netGaming inner join(select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT')orgDetails on netGaming.agent_org_id=orgDetails.agtOrgId) group by agent_org_id";
		System.out.println(commQuery);
		PreparedStatement pstmt = con.prepareStatement(commQuery);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int agtOrgId = rs.getInt("agent_org_id");
			OlaCommissionBean olaCommissionBean = new OlaCommissionBean();
			olaCommissionBean.setBoUserId(rs.getInt("boUserId"));
			olaCommissionBean.setBoUserOrgId(rs.getInt("boOrgId"));
			olaCommissionBean.setTotalPlayerNetGaming(rs
					.getDouble("plr_net_gaming"));
			olaCommissionBean.setTotalCommissionCalculated(rs
					.getDouble("commission_calculated"));
			olaCommissionBean.setId(rs.getInt("id"));
			olaMap.put(agtOrgId, olaCommissionBean);
		}
		return olaMap;
	}

	public static Map<Integer, OlaCommissionBean> getCommMonthlyWiseForAgent(
			Connection con, int walletId, Date startDate, Date endDate)
			throws SQLException {
		Map<Integer, OlaCommissionBean> olaMap = new TreeMap<Integer, OlaCommissionBean>();
		String commQuery = "select id,sum(net_gaming) plr_net_gaming,sum(comm_amt) commission_calculated,agent_org_id,boUserId,boOrgId from ((select id,sum(net_gaming) net_gaming,sum(comm_amt) comm_amt,agent_org_id from st_ola_agent_monthly_commission_exp where status='PENDING' and date>='"+ startDate+ "' and  date<='"+ endDate+ "' and wallet_id="+walletId+" group by agent_org_id)netGaming inner join(select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT')orgDetails on netGaming.agent_org_id=orgDetails.agtOrgId) group by agent_org_id";
		System.out.println(commQuery);
		PreparedStatement pstmt = con.prepareStatement(commQuery);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int agtOrgId = rs.getInt("agent_org_id");
			OlaCommissionBean olaCommissionBean = new OlaCommissionBean();
			olaCommissionBean.setBoUserId(rs.getInt("boUserId"));
			olaCommissionBean.setBoUserOrgId(rs.getInt("boOrgId"));
			olaCommissionBean.setTotalPlayerNetGaming(rs
					.getDouble("plr_net_gaming"));
			olaCommissionBean.setTotalCommissionCalculated(rs
					.getDouble("commission_calculated"));
			olaCommissionBean.setId(rs.getInt("id"));
			olaMap.put(agtOrgId, olaCommissionBean);
		}
		return olaMap;
	}

	public static int updateAgentBackOfficeDetails(Connection con,
			int agentOrgId, int walletId, double plrNetGaming,
			double commissionCalculated, Date startDate, Date endDate,
			int boUserID, int boUserOrgId, String updateMode , String updateType , int generatedId) 
			{
		int isUpdate = 0;
		int id = 0;
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			double agtCommRate = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, agentOrgId, "NETGAMING", "AGENT", con);
			String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('BO','OLA','WEB')";
			PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
			long transactionId = 0;
			pstmt1.executeUpdate();
			ResultSet rs1 = pstmt1.getGeneratedKeys();
			if (rs1.next()) {
				transactionId = rs1.getLong(1);
				pstmt1 = con
						.prepareStatement("INSERT INTO st_lms_bo_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
				pstmt1.setLong(1, transactionId);
				pstmt1.setInt(2, boUserID);
				pstmt1.setInt(3, boUserOrgId);
				pstmt1.setString(4, "AGENT");
				pstmt1.setInt(5, agentOrgId);
				pstmt1.setString(6, "OLA_COMMISSION");
				pstmt1.setTimestamp(7, new java.sql.Timestamp(new Date()
						.getTime()));

				pstmt1.executeUpdate();
			}
			
			PreparedStatement pstmt2 = con.prepareStatement("insert into st_ola_bo_agt_commission (agt_org_id, net_gaming, commission_calculated, comm_rate, transaction_id, start_date, end_date,wallet_id,update_mode,status)values(?,?,?,?,?,?,?,?,?,?)");
			pstmt2.setInt(1, agentOrgId);
			pstmt2.setDouble(2, plrNetGaming);
			pstmt2.setDouble(3, commissionCalculated);
			pstmt2.setDouble(4, agtCommRate);
			pstmt2.setLong(5, transactionId);
			pstmt2.setDate(6, new java.sql.Date(startDate.getTime()));
			pstmt2.setDate(7, new java.sql.Date(endDate.getTime()));
			pstmt2.setInt(8, walletId);
			pstmt2.setString(9, updateMode);
			pstmt2.setString(10, "DONE_CLAIM");
			isUpdate = pstmt2.executeUpdate();
			
			if (isUpdate == 1 && updateType.equalsIgnoreCase("WEEKLY")) {
				pstmt1 = con.prepareStatement("update st_ola_agent_weekly_commission_exp set status='DONE',refTransactionId = "+ transactionId+ ", updated_date='"+ new java.sql.Timestamp(new Date().getTime())+"' , updated_mode='"+updateMode+"' where id="+generatedId+"");
				pstmt1.executeUpdate();
			}
			else if(isUpdate == 1 && updateType.equalsIgnoreCase("MONTHLY"))
			{
				pstmt1 = con.prepareStatement("update st_ola_agent_monthly_commission_exp set status='DONE',refTransactionId = "+ transactionId+ ", updated_date='"+ new java.sql.Timestamp(new Date().getTime())+"' , updated_mode='"+updateMode+"' where id="+generatedId+"");
				pstmt1.executeUpdate();
			}
			
			// update retailers available balance
		 
			OrgCreditUpdation.updateCreditLimitForAgent(agentOrgId,
					"OLA_COMMISSION", commissionCalculated, con);
			// Manual request is made for updation automatically

			// insert into receipt master
			PreparedStatement preState4 = con.prepareStatement(QueryManager
					.insertInReceiptMaster());
			preState4.setString(1, "BO");
			preState4.executeUpdate();

			ResultSet rs12 = preState4.getGeneratedKeys();
			rs12.next();
			id = rs12.getInt(1);

			// get auto generated receipt number

			PreparedStatement preState5 = con.prepareStatement(QueryManager
					.getBOLatestReceiptNb());
			preState5.setString(1, "RECEIPT");
			ResultSet recieptRs = preState5.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
					"RECEIPT", lastRecieptNoGenerated, "BO");

			// insert in st bo receipts
			PreparedStatement preState6 = con.prepareStatement(QueryManager.insertInBOReceipts());

			preState6.setInt(1, id);
			preState6.setString(2, "RECEIPT");
			preState6.setInt(3, agentOrgId);
			preState6.setString(4, "AGENT");
			preState6.setString(5, autoGeneRecieptNo);
			preState6.setTimestamp(6, Util.getCurrentTimeStamp());

			preState6.executeUpdate();

			PreparedStatement preState3 = con.prepareStatement(QueryManager
					.insertBOReceiptTrnMapping());
			preState3.setInt(1, id);
			preState3.setLong(2, transactionId);
			preState3.executeUpdate();
			
			if (isUpdate == 1 && updateType.equalsIgnoreCase("WEEKLY")) {
				PreparedStatement	pstmt = con.prepareStatement("update st_ola_agent_weekly_commission_exp set credit_note_number = ? where id="+generatedId+"");
				pstmt.setString(1, autoGeneRecieptNo);
				pstmt.executeUpdate();
			}
			else if(isUpdate == 1 && updateType.equalsIgnoreCase("MONTHLY"))
			{
				PreparedStatement	pstmt = con.prepareStatement("update st_ola_agent_monthly_commission_exp set credit_note_number = ? where id="+generatedId+"");
				pstmt.setString(1, autoGeneRecieptNo);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		try {
			throw new LMSException();
		} catch (LMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		return isUpdate;
	}
	
	public static void updateNetGamingDataMonthlyWise() {
		Connection con = DBConnect.getConnection();
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.add(Calendar.DAY_OF_MONTH, -2);
		calEnd.add(Calendar.DAY_OF_MONTH, -2);
		java.sql.Date startDate1 = new java.sql.Date(calStart
				.getTime().getTime()); //by neeraj
		calStart.add(Calendar.MONTH, -1); // by neeraj 
		java.sql.Date startDate = new java.sql.Date(calStart
				.getTime().getTime());
		// calEnd.add(Calendar.MONTH, 1);
		calEnd.add(Calendar.DAY_OF_MONTH, -1);//by neeraj
		java.sql.Date endDate = new java.sql.Date(calEnd
				.getTime().getTime());
		Double commAmt = 0.0;
		try {
			con.setAutoCommit(false);
			String walletQry = "select wallet_id, wallet_name from st_ola_wallet_master";
			PreparedStatement walletPstmt = con.prepareStatement(walletQry);
			ResultSet rsWallet = walletPstmt.executeQuery();
			while (rsWallet.next()) {
				int walletId = rsWallet.getInt("wallet_id");
			
			//For Agent
			PreparedStatement pstmt = con.prepareStatement("select sum(plr_net_gaming) plr_net_gaming,agentOrgId from (select sum(plr_net_gaming) plr_net_gaming,ret_org_id from st_ola_daily_retailer_commission_"+walletId+" where status='PENDING' and date>='"+startDate+"' and  date<='"+endDate+"' group by ret_org_id)netGaming inner join (select um.user_id retailerUserId,um.organization_id retalierOrgId,um.parent_user_id agentUserId,om.parent_id agentOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') om on ret_org_id=retalierOrgId group by agentOrgId");
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				double agtCommRate = CommonFunctionsHelper.fetchOLACommOfOrganization(walletId, rs.getInt("agentOrgId"), "NETGAMING", "AGENT", con);
				commAmt = agtCommRate*(rs.getDouble("plr_net_gaming"))*.01;
				PreparedStatement pstmt2 = con.prepareStatement("insert into st_ola_agent_monthly_commission_exp(wallet_id, date, agent_org_id, net_gaming, comm_rate, comm_amt, refTransactionId, status) values (?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt2.setInt(1, walletId);
			//	pstmt2.setDate(2, endDate);
				pstmt2.setDate(2, startDate1);// by neeraj
				pstmt2.setInt(3, rs.getInt("agentOrgId"));
				pstmt2.setDouble(4, rs.getDouble("plr_net_gaming"));
				pstmt2.setDouble(5, agtCommRate);
				pstmt2.setDouble(6, commAmt);
				pstmt2.setInt(7, 0);
				pstmt2.setString(8, "PENDING");
				pstmt2.executeUpdate();
			}
			
			
			//For Retailer
			PreparedStatement pstmt3 = con.prepareStatement("select ret_org_id,sum(plr_net_gaming) net_gaming from st_ola_daily_retailer_commission_"+walletId+" where date>='"+startDate+"' and date<='"+endDate+"' and status='PENDING' group by ret_org_id");
		
			
			ResultSet rs1 = pstmt3.executeQuery();
			while(rs1.next())
			{
				double retCommRate = CommonFunctionsHelper.fetchOLACommOfOrganization(walletId, rs1.getInt("ret_org_id"), "NETGAMING", "RETAILER", con);
				commAmt = retCommRate*(rs1.getDouble("net_gaming"))*.01;
				PreparedStatement pstmt4 = con.prepareStatement("insert into st_ola_retailer_monthly_commission_exp(wallet_id, date, retailer_org_id, net_gaming, comm_rate, comm_amt, refTransactionId, status) values (?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt4.setInt(1, walletId);
			//	 pstmt4.setDate(2, endDate);
				pstmt4.setDate(2,startDate1);// by neeraj
				pstmt4.setInt(3, rs1.getInt("ret_org_id"));
				pstmt4.setDouble(4, rs1.getDouble("net_gaming"));
				pstmt4.setDouble(5, retCommRate);
				pstmt4.setDouble(6, commAmt);
				pstmt4.setInt(7, 0);
				pstmt4.setString(8, "PENDING");
				pstmt4.executeUpdate();
				
				// Added To Make Status Done 
				PreparedStatement pstmt5 = con.prepareStatement("update st_ola_daily_retailer_commission_"+walletId+" set status='DONE' where date>='"+startDate+"' and date<='"+endDate+"' and status='PENDING' and ret_org_id = "+rs1.getInt("ret_org_id")+"");
				pstmt5.executeUpdate();
			}
			
			
			}
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw new LMSException();
			} catch (LMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {

			try {
				if (con != null) {
					
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					throw new LMSException();
				} catch (LMSException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
	}

	public static void updateNetGamingDataWeeklyWise() {
		Connection con = DBConnect.getConnection();
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.add(Calendar.DAY_OF_MONTH, -2);
		calEnd.add(Calendar.DAY_OF_MONTH, -2);
		java.sql.Date startDate1 = new java.sql.Date(calStart
				.getTime().getTime());//by neeraj
		calStart.add(Calendar.WEEK_OF_MONTH, -1); //  by neeraj 
		java.sql.Date startDate = new java.sql.Date(calStart
				.getTime().getTime());
		
		calEnd.add(Calendar.DAY_OF_MONTH,-1); // value changed from 1 to -1 by neeraj 
		
		java.sql.Date endDate = new java.sql.Date(calEnd
				.getTime().getTime());
		double commAmt = 0.0;
		try {
			//set auto commit to false ...
			con.setAutoCommit(false);
			String walletQry = "select wallet_id, wallet_name from st_ola_wallet_master";
			PreparedStatement walletPstmt = con.prepareStatement(walletQry);
			ResultSet rsWallet = walletPstmt.executeQuery();
			while (rsWallet.next()) {
				int walletId = rsWallet.getInt("wallet_id");
			
			//For Agent
			PreparedStatement pstmt = con.prepareStatement("select sum(plr_net_gaming) plr_net_gaming,agentOrgId from (select sum(plr_net_gaming) plr_net_gaming,ret_org_id from st_ola_daily_retailer_commission_"+walletId+" where status='PENDING' and date>='"+startDate+"' and  date<='"+endDate+"' group by ret_org_id)netGaming inner join (select um.user_id retailerUserId,um.organization_id retalierOrgId,um.parent_user_id agentUserId,om.parent_id agentOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') om on ret_org_id=retalierOrgId group by agentOrgId");
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				double agtCommRate = CommonFunctionsHelper.fetchOLACommOfOrganization(walletId, rs.getInt("agentOrgId"), "NETGAMING", "AGENT", con);
				commAmt = agtCommRate*(rs.getDouble("plr_net_gaming"))*.01;
				PreparedStatement pstmt2 = con.prepareStatement("insert into st_ola_agent_weekly_commission_exp(wallet_id, date, agent_org_id, net_gaming, comm_rate, comm_amt, refTransactionId, status) values (?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt2.setInt(1, walletId);
				pstmt2.setDate(2, startDate1);//change endDate to startDate1 by neeraj
				pstmt2.setInt(3, rs.getInt("agentOrgId"));
				pstmt2.setDouble(4, rs.getDouble("plr_net_gaming"));
				pstmt2.setDouble(5, agtCommRate);
				pstmt2.setDouble(6, commAmt);
				pstmt2.setInt(7, 0);
				pstmt2.setString(8, "PENDING");
				pstmt2.executeUpdate();
			
			}
			
			
			//For Retailer
			PreparedStatement pstmt3 = con.prepareStatement("select ret_org_id,sum(plr_net_gaming) net_gaming from st_ola_daily_retailer_commission_"+walletId+" where date>='"+startDate+"' and date<='"+endDate+"' and status='PENDING' group by ret_org_id");
			ResultSet rs1 = pstmt3.executeQuery();
			while(rs1.next())
			{
				double retCommRate = CommonFunctionsHelper.fetchOLACommOfOrganization(walletId, rs1.getInt("ret_org_id"), "NETGAMING", "RETAILER", con);
				commAmt = retCommRate*(rs1.getDouble("net_gaming"))*.01;
				PreparedStatement pstmt4 = con.prepareStatement("insert into st_ola_retailer_weekly_commission_exp(wallet_id, date, retailer_org_id, net_gaming, comm_rate, comm_amt, refTransactionId, status)values(?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt4.setInt(1, walletId);
				pstmt4.setDate(2, startDate1);//change endDate to startDate1 by neeraj
				pstmt4.setInt(3, rs1.getInt("ret_org_id"));
				pstmt4.setDouble(4, rs1.getDouble("net_gaming"));
				pstmt4.setDouble(5, retCommRate);
				pstmt4.setDouble(6, commAmt);
				pstmt4.setInt(7, 0);
				pstmt4.setString(8, "PENDING");
				pstmt4.executeUpdate();
				
				// Added To Make Status Done 
				PreparedStatement pstmt5 = con.prepareStatement("update st_ola_daily_retailer_commission_"+walletId+" set status='DONE' where date>='"+startDate+"' and date<='"+endDate+"' and status='PENDING' and ret_org_id = "+rs1.getInt("ret_org_id")+"");
				pstmt5.executeUpdate();
			}
				
			}
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw new LMSException();
			} catch (LMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {

			try {
				if (con != null) {					
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					throw new LMSException();
				} catch (LMSException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
	}



	
}