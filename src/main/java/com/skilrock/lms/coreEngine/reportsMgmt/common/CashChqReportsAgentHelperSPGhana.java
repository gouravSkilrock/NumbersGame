package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class CashChqReportsAgentHelperSPGhana implements ICashChqReportsAgentHelper{

	private int agentOrgId;
	private int agentUserId;
	private Connection con = null;
	private Date endDate = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Date startDate = null;
	public CashChqReportsAgentHelperSPGhana(){
		
	}
	public CashChqReportsAgentHelperSPGhana(UserInfoBean userInfoBean,DateBeans dateBeans) {
		this.agentUserId = userInfoBean.getUserId();
		this.agentOrgId = userInfoBean.getUserOrgId();
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		System.out.println(this.startDate + "  " + this.endDate + " userId "
				+ agentUserId + " agentOrgId " + agentOrgId);
	}

	public List<CashChqReportBean> getCashChqDetail() {
		System.out.println("in get cash chq DetailcashChqReportForAgentcashChqReportForAgent");
		Map<String,CashChqReportBean> retailerWiseMap = new HashMap<String, CashChqReportBean>();
		List<CashChqReportBean> list = null;
		CashChqReportBean reportbean = null;
		con = DBConnect.getConnection();
		List<Integer> retailerOrgIdList = getRetailerOrgId(con);
		System.out.println("retailer org id  list : " + retailerOrgIdList);
		//remove terminate Retailer
		OrganizationTerminateReportHelper.getTerminateRetailerListForRep(new Timestamp(startDate.getTime()),new Timestamp(endDate.getTime()),agentOrgId );
		List<Integer> terminateRetailerList=OrganizationTerminateReportHelper.RetailerOrgIdIntTypeList;
        System.out.println("Terminate agent List::"+terminateRetailerList);
        retailerOrgIdList.removeAll(terminateRetailerList);
        Map<Integer, String> retNameOrgIdMap  = new LinkedHashMap<Integer, String>();
        String balQuery = null;
  		PreparedStatement pstm1 = null;
  		ResultSet rs1 = null;
  		Map<String, Double> retBalMap = new HashMap<String, Double>();
		try {
			if(retailerOrgIdList.size()>0){
				ReportUtility.getOrgNameMap(con,retNameOrgIdMap,retailerOrgIdList.toString().replace("["," ").replace("]"," "));
			}
			balQuery = "Select concat(org_code,'_',name) name,IFNULL((available_credit - claimable_bal),0.0) balance From st_lms_organization_master where organization_type = 'RETAILER' and organization_status <> 'TERMINATE' and parent_id="+agentOrgId;
			pstm1 = con.prepareStatement(balQuery);
			rs1 = pstm1.executeQuery();
			while(rs1.next()){
				retBalMap.put(rs1.getString("name"), rs1.getDouble("balance"));
			}
			for (Integer retailerOrgId : retailerOrgIdList) {
				CallableStatement pstmt = con.prepareCall("{call getStCashCheqReportDetail(?,?,?,?)}");
				pstmt.setInt(1, agentOrgId);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);
				pstmt.setInt(4, retailerOrgId);
				System.out.println("query--" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					reportbean = new CashChqReportBean();
					reportbean.setTotalCash(FormatNumber.formatNumber(rs.getDouble("total_cash")));
					reportbean.setTotalChq(FormatNumber.formatNumber(rs.getDouble("chq_coll")));
					reportbean.setCheqBounce(FormatNumber.formatNumber(rs.getDouble("chq_bounce")));
					reportbean.setNetAmt(FormatNumber.formatNumber(rs.getDouble("net_amount")));
					reportbean.setName(retNameOrgIdMap.get(retailerOrgId.intValue()));
					reportbean.setCredit(FormatNumber.formatNumber(rs.getDouble("credit_amt")));
					reportbean.setDebit(FormatNumber.formatNumber(rs.getDouble("debit_amt")));
					retailerWiseMap.put(retNameOrgIdMap.get(retailerOrgId.intValue()),reportbean);
				}
			}
			//System.out.println("Ret Bal Map:"+retBalMap);
			Set<Map.Entry<String, Double>> entrySet = retBalMap.entrySet();
			for (Entry<String, Double> entry : entrySet) {
			  if(retailerWiseMap.containsKey(entry.getKey())){
				  retailerWiseMap.get(entry.getKey().toString()).setCurrentBal((Double) entry.getValue());
			  }else{
				  reportbean = new CashChqReportBean();
				  reportbean.setTotalCash("0.0");
				  reportbean.setTotalChq("0.0");
				  reportbean.setCheqBounce("0.0");
				  reportbean.setNetAmt("0.0");
				  reportbean.setCredit("0.0");
				  reportbean.setDebit("0.0");
				  reportbean.setName(entry.getKey().toString());
				  reportbean.setCurrentBal((Double) entry.getValue());
				  retailerWiseMap.put(entry.getKey().toString(),reportbean);
			  }
			}

			list = new ArrayList<CashChqReportBean>(retailerWiseMap.values());

			if( (LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("ORG_ID")){
				Collections.sort(list,new Comparator<CashChqReportBean>() {

					@Override
					public int compare(CashChqReportBean o1, CashChqReportBean o2) {
						if(o1.getOrgId()>o2.getOrgId()){
							return 1;
						}else {
							return -1;
						}
						
					}
				});
				
			}else if ((LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("DESC")) {

				Collections.sort(list, new Comparator<CashChqReportBean>() {

					@Override
					public int compare(CashChqReportBean o1, CashChqReportBean o2) {

						return (o2.getName().toUpperCase()).compareTo(o1.getName().toUpperCase());
					}
				});

			} else{
				Collections.sort(list,new Comparator<CashChqReportBean>() {

					@Override
					public int compare(CashChqReportBean o1, CashChqReportBean o2) {
						
						return (o1.getName().toUpperCase()).compareTo(o2.getName().toUpperCase());
					}
				});
				
			}
		//	System.out.println(list.size());
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBConnect.closeConnection(pstm1, rs1);
		}

		return list;
	}

	public CashChqReportBean getCashChqDetailWithBO() {

		CashChqReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			CallableStatement pstmt = con
			.prepareCall("{call getStCashCheqReportBo2(?,?,?)}");
			pstmt.setInt(1, agentOrgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			System.out.println("Query : " + pstmt);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportbean = new CashChqReportBean();

				reportbean.setTotalCash(FormatNumber.formatNumber(rs
						.getDouble("total_cash")));
				reportbean.setTotalChq(FormatNumber.formatNumber(rs
						.getDouble("cheque_coll")));
				reportbean.setDebit(FormatNumber.formatNumber(rs
						.getDouble("debit")));
				reportbean.setCredit(FormatNumber.formatNumber(rs
						.getDouble("credit")));
				reportbean.setCheqBounce(FormatNumber.formatNumber(rs
						.getDouble("bounce")));
				reportbean.setBankDeposit(FormatNumber.formatNumber(rs
						.getDouble("bank_amt")));
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("net_amount")));
				// System.out.print("\nCash : "+reportbean.getTotalCash()+",
				// Total Cheque : "+reportbean.getTotalChq()+", Cheque Bounce :
				// "+reportbean.getCheqBounce());
				// if(Double.parseDouble(reportbean.getTotalCash())==0 &&
				// Double.parseDouble(reportbean.getTotalChq())==0 &&
				// Double.parseDouble(reportbean.getCheqBounce())==0 );
				// reportbean=null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reportbean;
	}

	public String getRetailerName(Connection conn, int agentid) {
		String aname = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(QueryManager
					.getST_GET_ORG_NAME());
			pstmt.setInt(1, agentid);
			System.out.println("query in agentid--" + pstmt);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				aname = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return aname;
	}

	public List<Integer> getRetailerOrgId(Connection conn) {
		List<Integer> list = new ArrayList<Integer>();

		try {
			con = conn;
			CallableStatement pstmt = con
			.prepareCall("{call getStCashCheqReportRetailerId(?,?,?)}");
			pstmt.setInt(1, agentOrgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			System.out.println("cash cheque query ====== : " + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("retailer_org_id");
				// System.out.print("Retailer org id : "+id);
				list.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(pstmt,rs);
		}

		return list;
	}
	public Map<Integer,String> getUserList() 
	{
		Map<Integer,String> userMap = new HashMap<Integer,String>();
		Connection conn = null;
		try{
		conn = DBConnect.getConnection();
		PreparedStatement statement = conn.prepareStatement("select user_id,user_name from st_lms_user_master where organization_type='BO'");
		ResultSet rs = statement.executeQuery();
		while(rs.next())
		{
			userMap.put(rs.getInt("user_id"), rs.getString("user_name"));
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			DBConnect.closeCon(conn);
		}
		return userMap;
	}
	/*@Override
	public List<CashChqReportBean> getCashChqDetail() {
		// TODO Auto-generated method stub
		return null;
	}*/
	
}
