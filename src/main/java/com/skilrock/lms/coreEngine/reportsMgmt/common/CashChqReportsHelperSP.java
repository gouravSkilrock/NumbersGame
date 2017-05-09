package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.CashChqPmntBean;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;

public class CashChqReportsHelperSP implements ICashChqReportsHelper {

	private Connection con = null;
	private Date endDate;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Date startDate;

	public CashChqReportsHelperSP(DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		System.out.println(this.startDate + "  " + this.endDate);
	}

	private void fillReportBean(CashChqReportBean reportbean) {
		reportbean.setTotalCash("0.0");
		reportbean.setTotalChq("0.0");
		reportbean.setDebit("0.0");
		reportbean.setCredit("0.0");
		reportbean.setCheqBounce("0.0");
		reportbean.setNetAmt("0.0");
		reportbean.setBankDeposit("0.0");
	}

	private void fillReportBean(CashChqReportBean reportbean, String trxType,
			Double value) {
		if (trxType.equals("CASH")) {
			reportbean.setTotalCash(FormatNumber.formatNumber(value));
		} else if (trxType.equals("CHEQUE")) {
			reportbean.setTotalChq(FormatNumber.formatNumber(value));
		} else if (trxType.equals("DEBIT_NOTE")) {
			reportbean.setDebit(FormatNumber.formatNumber(value));
		} else if (trxType.equals("CREDIT_NOTE")) {
			reportbean.setCredit(FormatNumber.formatNumber(value));
		} else if (trxType.equals("CHQ_BOUNCE")) {
			reportbean.setCheqBounce(FormatNumber.formatNumber(value));
		} else if (trxType.equals("BANK_DEPOSIT")) {
			reportbean.setBankDeposit(FormatNumber.formatNumber(value));
		}
	}

	public List<Integer> getAgentId() throws LMSException {
		List<Integer> list = new ArrayList<Integer>();

		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(QueryManager
					.getST_CASH_CHEQ_REPORT_BO1());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			pstmt.setDate(5, startDate);
			pstmt.setDate(6, endDate);
			pstmt.setDate(7, startDate);
			pstmt.setDate(8, endDate);
			ResultSet rss = pstmt.executeQuery();
			System.out.println("get agent org ids details  query : " + pstmt);
			while (rss.next()) {
				int id = rss.getInt("agent_org_id");
				// System.out.print(", agent org id : "+id);
				list.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
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
				throw new LMSException(e);
			}
		}

		return list;
	}

	public String getAgentName(Connection conn, int agentid)
			throws SQLException {
		String aname = null;
		pstmt = conn.prepareStatement(QueryManager.getST_GET_ORG_NAME());
		pstmt.setInt(1, agentid);
		System.out.println("query in agent name--"
				+ QueryManager.getST_GET_ORG_NAME());
		ResultSet rss = pstmt.executeQuery();
		while (rss.next()) {
			aname = rss.getString("name");
		}

		return aname;
	}

	public String getAgentName(int agentid,Connection con) {
		String aname = null;
		try {
			
			pstmt = con.prepareStatement(QueryManager.getST_GET_ORG_NAME());
			pstmt.setInt(1, agentid);
			//System.out.println("query in agent name--"
				//	+ QueryManager.getST_GET_ORG_NAME());
			ResultSet rss = pstmt.executeQuery();
			while (rss.next()) {
				aname = rss.getString("name");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return aname;
	}

	public List<Integer> getAgentOrgList() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(QueryManager
					.getST_RECEIPT_SEARCH_AGENT_ORGID());
			ResultSet result = pstmt.executeQuery();
			while (result.next()) {
				list.add(result.getInt(TableConstants.ORG_ID));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<CashChqReportBean> getCashChqDetail(List<Integer> agtidlist,int userId,boolean isExpand)
			throws LMSException {
		List<CashChqReportBean> list = new ArrayList<CashChqReportBean>();
		CashChqReportBean reportbean = null;
		con = DBConnect.getConnection();
		System.out.println("size of agent org id list " + agtidlist.size());

		try {
			for (Integer agentorgId : agtidlist) {
				CallableStatement pstmt = con.prepareCall("{call getStCashCheqReportBo3(?,?,?,?,"+isExpand+")}");
				pstmt.setInt(1, agentorgId);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);
				pstmt.setInt(4, userId);
				rs = pstmt.executeQuery();
				System.out.println("get cash cheque details Query : " + pstmt);
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
							.getDouble("bank_deposit")));
					reportbean.setNetAmt(FormatNumber.formatNumber(rs
							.getDouble("net_amount")));
					reportbean.setName(getAgentName(con, agentorgId));
					reportbean.setOrgId(agentorgId);
					list.add(reportbean);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
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
				throw new LMSException(e);
			}
		}

		return list;
	}

	public List<CashChqPmntBean> getCashChqDetailAgentWise(String startDate,String endDate,int userId,boolean isExpand, String state, String city) throws LMSException {
		List<CashChqPmntBean> list = new ArrayList<CashChqPmntBean>();
		CashChqPmntBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			CallableStatement pstmt = con.prepareCall("{call getCashChqDetailAgentWise(?,?,?,"+isExpand+")}");
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			pstmt.setInt(3, userId);
			rs = pstmt.executeQuery();
			// System.out.println("result set : --------------"+rs);
			System.out.println("get cash cheque details Query : " + pstmt);
			while (rs.next()) {
				reportbean = new CashChqPmntBean();

				reportbean.setPaymentAmount(rs.getDouble("amount"));
				reportbean.setPaymentType(rs.getString("type"));
				reportbean.setVouncherNo(rs.getString("generated_id"));
				reportbean.setDate(rs.getString("name"));
				list.add(reportbean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
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
				throw new LMSException(e);
			}
		}

		return list;
	}

	public List<CashChqPmntBean> getCashChqDetailDateWise(int orgId,int userId)
			throws LMSException {
		List<CashChqPmntBean> list = new ArrayList<CashChqPmntBean>();
		CashChqPmntBean reportbean = null;
		con = DBConnect.getConnection();
		System.out.println("Agent Date wise report for --> " + orgId);
	

		try {
			CallableStatement pstmt = con.prepareCall("{call getCashChqDetailDateWise(?,?,?,?)}");
			pstmt.setInt(1, orgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			pstmt.setInt(4, userId);
			rs = pstmt.executeQuery();
			// System.out.println("result set : --------------"+rs);
			System.out.println("get cash cheque details Query : " + pstmt);
			while (rs.next()) {
				reportbean = new CashChqPmntBean();

				reportbean.setPaymentAmount(rs.getDouble("amount"));
				reportbean.setPaymentType(rs.getString("type"));
				reportbean.setVouncherNo(rs.getString("generated_id"));
				reportbean.setDate(rs.getString("transaction_date").replace(
						".0", ""));
				list.add(reportbean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
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
				throw new LMSException(e);
			}
		}

		return list;
	}

	public List<CashChqReportBean> getCashChqDetailDayWise(int userId,boolean isExpand)
			throws LMSException {
		List<CashChqReportBean> list = new ArrayList<CashChqReportBean>();
		Map<String, CashChqReportBean> repMap = new LinkedHashMap<String, CashChqReportBean>();
		CashChqReportBean reportbean = null;
		String date = null;
		con = DBConnect.getConnection();
		System.out.println(startDate + "Day wise report for --> " + endDate);

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		while (startCal.getTime().before(endCal.getTime())) {
			date = new java.sql.Date(startCal.getTimeInMillis()).toString();
			reportbean = new CashChqReportBean();
			fillReportBean(reportbean);
			reportbean.setName(date);
			repMap.put(date, reportbean);
			startCal.setTimeInMillis(startCal.getTimeInMillis() + 24 * 60 * 60
					* 1000);

		}
		try {
		CallableStatement pstmt = con.prepareCall("{call getCashChqDetailDayWise(?,?,?,"+isExpand+")}");
		pstmt.setDate(1, startDate);
		pstmt.setDate(2, endDate);
		pstmt.setInt(3, userId);
			rs = pstmt.executeQuery();
			System.out.println("get cash cheque details Query : " + pstmt);
			while (rs.next()) {
				reportbean = repMap.get(rs.getString("transaction_date"));
				fillReportBean(reportbean, rs.getString("type"), rs
						.getDouble("amount"));
			}
			list.addAll(repMap.values());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
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
				throw new LMSException(e);
			}
		}

		return list;
	}

}
