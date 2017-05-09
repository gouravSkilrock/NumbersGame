package com.skilrock.lms.coreEngine.reportsMgmt.common;

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

import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class CashChqReportsAgentHelper implements ICashChqReportsAgentHelper {

	private int agentOrgId;
	private int agentUserId;
	private Connection con = null;
	private Date endDate = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Date startDate = null;

	public CashChqReportsAgentHelper() {

	}

	public CashChqReportsAgentHelper(UserInfoBean userInfoBean,
			DateBeans dateBeans) {
		this.agentUserId = userInfoBean.getUserId();
		this.agentOrgId = userInfoBean.getUserOrgId();
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		System.out.println(this.startDate + "  " + this.endDate + " userId "
				+ agentUserId + " agentOrgId " + agentOrgId);
	}

	public List<CashChqReportBean> getCashChqDetail() {
		List<CashChqReportBean> list = new ArrayList<CashChqReportBean>();
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
		
		
		try {
			if(retailerOrgIdList.size()>0){
			ReportUtility.getOrgNameMap(con,retNameOrgIdMap,retailerOrgIdList.toString().replace("["," ").replace("]"," "));
			}
			for (Integer retailerOrgId : retailerOrgIdList) {
				pstmt = con.prepareStatement(QueryManager
						.getST_CASH_CHEQ_REPORT_DETAIL());
				pstmt.setInt(1, agentOrgId);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);
				pstmt.setInt(4, retailerOrgId);

				pstmt.setInt(5, agentOrgId);
				pstmt.setDate(6, startDate);
				pstmt.setDate(7, endDate);
				pstmt.setInt(8, retailerOrgId);

				pstmt.setInt(9, agentOrgId);
				pstmt.setDate(10, startDate);
				pstmt.setDate(11, endDate);
				pstmt.setInt(12, retailerOrgId);

				pstmt.setInt(13, agentOrgId);
				pstmt.setInt(14, retailerOrgId);
				pstmt.setDate(15, startDate);
				pstmt.setDate(16, endDate);

				pstmt.setInt(17, agentOrgId);
				pstmt.setInt(18, retailerOrgId);
				pstmt.setDate(19, startDate);
				pstmt.setDate(20, endDate);

				System.out.println("query--" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					reportbean = new CashChqReportBean();

					reportbean.setTotalCash(FormatNumber.formatNumber(rs
							.getDouble("total_cash")));
					reportbean.setTotalChq(FormatNumber.formatNumber(rs
							.getDouble("chq_coll")));
					reportbean.setCheqBounce(FormatNumber.formatNumber(rs
							.getDouble("chq_bounce")));
					reportbean.setNetAmt(FormatNumber.formatNumber(rs
							.getDouble("net_amount")));
			/*		reportbean.setName(getRetailerName(con, retailerOrgId
							.intValue()));*/
					reportbean.setName(retNameOrgIdMap.get(retailerOrgId.intValue()));
					reportbean.setOrgId(retailerOrgId.intValue());
					reportbean.setCredit(FormatNumber.formatNumber(rs
							.getDouble("credit_amt")));
					reportbean.setDebit(FormatNumber.formatNumber(rs
							.getDouble("debit_amt")));
					// System.out.print("\nRetailer Org ID :
					// "+reportbean.getName()+", cash :
					// "+reportbean.getTotalCash()+", Total Cheque :
					// "+reportbean.getTotalChq()+", Cheque Bounce :
					// "+reportbean.getCheqBounce());
					list.add(reportbean);
				}
			}
			
			
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

		return list;
	}

	public CashChqReportBean getCashChqDetailWithBO() {

		CashChqReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			pstmt = con.prepareStatement(QueryManager
					.getST_CASH_CHEQ_REPORT_BO2());
			pstmt.setInt(1, agentOrgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			pstmt.setInt(4, agentOrgId);
			pstmt.setDate(5, startDate);
			pstmt.setDate(6, endDate);
			pstmt.setInt(7, agentOrgId);
			pstmt.setDate(8, startDate);
			pstmt.setDate(9, endDate);
			pstmt.setInt(10, agentOrgId);
			pstmt.setDate(11, startDate);
			pstmt.setDate(12, endDate);
			pstmt.setInt(13, agentOrgId);
			pstmt.setDate(14, startDate);
			pstmt.setDate(15, endDate);

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
				reportbean.setBankDeposit("0.00");
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
			pstmt = con.prepareStatement(QueryManager
					.getST_CASH_CHEQ_REPORT_RETAILER_ID());
			pstmt.setInt(1, agentOrgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);

			pstmt.setInt(4, agentOrgId);
			pstmt.setDate(5, startDate);
			pstmt.setDate(6, endDate);

			pstmt.setInt(7, agentOrgId);
			pstmt.setDate(8, startDate);
			pstmt.setDate(9, endDate);

			pstmt.setInt(10, agentOrgId);
			pstmt.setDate(11, startDate);
			pstmt.setDate(12, endDate);

			System.out.println("cash cheque query ====== : " + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("retailer_org_id");
				// System.out.print("Retailer org id : "+id);
				list.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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

}
