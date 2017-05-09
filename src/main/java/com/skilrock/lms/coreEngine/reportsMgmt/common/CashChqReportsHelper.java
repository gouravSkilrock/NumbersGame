package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.write.WriteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CashChqPmntBean;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.CashierDrawerDataForPWTBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;
import com.skilrock.lms.web.reportsMgmt.common.WriteExcelForCashChq;

public class CashChqReportsHelper implements ICashChqReportsHelper {

	private Connection con = null;
	private Date endDate;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Date startDate;
	Log logger = LogFactory.getLog(CashChqReportsHelper.class);
	public CashChqReportsHelper(Date firstDate, Date lastDate) {
		this.startDate = firstDate;
		this.endDate = lastDate;
	}

	public CashChqReportsHelper(DateBeans dateBeans) {
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

	/**
	 * @deprecated Use getCashChqDetail() Method Instead
	 */
	public List<CashChqReportBean> getCashChqDetail(List<Integer> agtidlist,int userId, boolean isExpand) throws LMSException {
		List<CashChqReportBean> list = new ArrayList<CashChqReportBean>();
		CashChqReportBean reportbean = null;
		con = DBConnect.getConnection();
		System.out.println("size of agent org id list " + agtidlist.size());
		StringBuilder query = null;
		double totalCash = 0;
		double totalCheque = 0;
		double totalDebit = 0;
		double totalCredit = 0;
		double totalBounce = 0;
		double totalDeposit = 0;
		double totalAmount = 0;
		Map<Integer, String> agtNameOrgIdMap = new LinkedHashMap<Integer, String>();

		try {
			if (agtidlist.size() > 0) {
				ReportUtility.getOrgNameMap(con, agtNameOrgIdMap, agtidlist
						.toString().replace("[", " ").replace("]", " "));
			}
			if (!isExpand) {
				query = new StringBuilder(
						"select sum(total_cash) 'total_cash', sum(credit) 'credit', sum(debit) 'debit', sum(cheque_coll) 'cheque_coll', sum(bounce) 'bounce',sum(bank_deposit) bank_deposit,sum(net_amount) 'net_amount' from (select aa.a 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit', dd.credit_amt 'credit',bd.d 'bank_deposit', ((aa.a+bb.b+dd.credit_amt+bd.d)-(cc.c+gg.debit_amt))'net_amount' from (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and chq.transaction_id=btm.transaction_id ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and chq.transaction_id=btm.transaction_id ) cc,(select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) )dd,(select ifnull(sum(bank.amount),0) 'd' from st_lms_bo_bank_deposit_transaction bank, st_lms_bo_transaction_master btm where bank.agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and bank.transaction_id=btm.transaction_id ) bd)) ee");
				if (LMSFilterDispatcher.isRepFrmSP) {
					query
							.append(" union all select sum(cash_amt) 'total_cash', sum(credit_note) 'credit', sum(debit_note) 'debit', sum(cheque_amt) 'cheque_coll', sum(cheque_bounce_amt) 'bounce', sum(bank_deposit) 'bank_deposit',sum(((cash_amt+cheque_amt+credit_note+bank_deposit)-(cheque_bounce_amt+debit_note))) as 'net_amount' from st_rep_bo_payments where agent_org_id = ? and finaldate >= ? and finaldate <= ?");
				}
				for (Integer agentorgId : agtidlist) {
					totalCash = 0;
					totalCheque = 0;
					totalDebit = 0;
					totalCredit = 0;
					totalBounce = 0;
					totalDeposit = 0;
					totalAmount = 0;
					pstmt = con.prepareStatement(query.toString());
					pstmt.setInt(1, agentorgId);
					pstmt.setInt(2, userId);
					pstmt.setDate(3, startDate);
					pstmt.setDate(4, endDate);
					pstmt.setInt(5, agentorgId);
					pstmt.setInt(6, userId);
					pstmt.setDate(7, startDate);
					pstmt.setDate(8, endDate);
					pstmt.setInt(9, agentorgId);
					pstmt.setInt(10, userId);
					pstmt.setDate(11, startDate);
					pstmt.setDate(12, endDate);
					pstmt.setInt(13, agentorgId);
					pstmt.setInt(14, userId);
					pstmt.setDate(15, startDate);
					pstmt.setDate(16, endDate);
					pstmt.setInt(17, agentorgId);
					pstmt.setInt(18, userId);
					pstmt.setDate(19, startDate);
					pstmt.setDate(20, endDate);
					pstmt.setInt(21, agentorgId);
					pstmt.setInt(22, userId);
					pstmt.setDate(23, startDate);
					pstmt.setDate(24, endDate);
					if (LMSFilterDispatcher.isRepFrmSP) {
						pstmt.setInt(25, agentorgId);
						pstmt.setDate(26, startDate);
						pstmt.setDate(27, endDate);
					}
					rs = pstmt.executeQuery();
					// System.out.println("result set : --------------"+rs);
					// System.out.println("get cash cheque details Query : " +
					// pstmt);
					while (rs.next()) {
						totalCash += rs.getDouble("total_cash");
						totalCheque += rs.getDouble("cheque_coll");
						totalDebit += rs.getDouble("debit");
						totalCredit += rs.getDouble("credit");
						totalBounce += rs.getDouble("bounce");
						totalDeposit += rs.getDouble("bank_deposit");
						totalAmount += rs.getDouble("net_amount");
					}
					reportbean = new CashChqReportBean();
					reportbean.setTotalCash(FormatNumber
							.formatNumber(totalCash));
					reportbean.setTotalChq(FormatNumber
							.formatNumber(totalCheque));
					reportbean.setDebit(FormatNumber.formatNumber(totalDebit));
					reportbean
							.setCredit(FormatNumber.formatNumber(totalCredit));
					reportbean.setCheqBounce(FormatNumber
							.formatNumber(totalBounce));
					reportbean.setBankDeposit(FormatNumber
							.formatNumber(totalDeposit));
					reportbean
							.setNetAmt(FormatNumber.formatNumber(totalAmount));
					/*
					 * reportbean.setName(ReportUtility.getOrgNameFromOrgId(con,
					 * agentorgId));
					 */
					reportbean.setName(agtNameOrgIdMap.get(agentorgId));

					reportbean.setOrgId(agentorgId);
					list.add(reportbean);
				}
				
			} else {
				query = new StringBuilder(
						"select sum(total_cash) 'total_cash', sum(credit) 'credit', sum(debit) 'debit', sum(cheque_coll) 'cheque_coll', sum(bounce) 'bounce',sum(bank_deposit) bank_deposit,sum(net_amount) 'net_amount' from (select aa.a 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit', dd.credit_amt 'credit',bd.d 'bank_deposit', ((aa.a+bb.b+dd.credit_amt+bd.d)-(cc.c+gg.debit_amt))'net_amount' from (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id = ? and ( btm.transaction_date >= ? and btm.transaction_date < ?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id = ? and ( btm.transaction_date >= ? and btm.transaction_date < ?) and chq.transaction_id=btm.transaction_id ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id = ? and ( btm.transaction_date >= ? and btm.transaction_date < ?) and chq.transaction_id=btm.transaction_id ) cc,(select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id = ? and ( btm.transaction_date >= ? and btm.transaction_date < ?) )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and agent_org_id = ? and ( btm.transaction_date >= ? and btm.transaction_date < ?) )dd,(select ifnull(sum(bank.amount),0) 'd' from st_lms_bo_bank_deposit_transaction bank, st_lms_bo_transaction_master btm where bank.agent_org_id = ? and ( btm.transaction_date >= ? and btm.transaction_date < ?) and bank.transaction_id=btm.transaction_id ) bd)) ee");
				if (LMSFilterDispatcher.isRepFrmSP) {
					query
							.append(" union all select sum(cash_amt) 'total_cash',  sum(credit_note) 'credit', sum(debit_note) 'debit', sum(cheque_amt) 'cheque_coll', sum(cheque_bounce_amt) 'bounce', sum(bank_deposit) 'bank_deposit',sum(((cash_amt+cheque_amt+credit_note+bank_deposit)-(cheque_bounce_amt+debit_note))) as 'net_amount' from st_rep_bo_payments where agent_org_id = ? and finaldate >= ? and finaldate < ?");
				}
				for (Integer agentorgId : agtidlist) {
					totalCash = 0;
					totalCheque = 0;
					totalDebit = 0;
					totalCredit = 0;
					totalBounce = 0;
					totalDeposit = 0;
					totalAmount = 0;
					pstmt = con.prepareStatement(query.toString());
					pstmt.setInt(1, agentorgId);
					pstmt.setDate(2, startDate);
					pstmt.setDate(3, endDate);
					pstmt.setInt(4, agentorgId);
					pstmt.setDate(5, startDate);
					pstmt.setDate(6, endDate);
					pstmt.setInt(7, agentorgId);
					pstmt.setDate(8, startDate);
					pstmt.setDate(9, endDate);
					pstmt.setInt(10, agentorgId);
					pstmt.setDate(11, startDate);
					pstmt.setDate(12, endDate);
					pstmt.setInt(13, agentorgId);
					pstmt.setDate(14, startDate);
					pstmt.setDate(15, endDate);
					pstmt.setInt(16, agentorgId);
					pstmt.setDate(17, startDate);
					pstmt.setDate(18, endDate);
					if (LMSFilterDispatcher.isRepFrmSP) {
						pstmt.setInt(19, agentorgId);
						pstmt.setDate(20, startDate);
						pstmt.setDate(21, endDate);
					}
					rs = pstmt.executeQuery();
					// System.out.println("result set : --------------"+rs);
					// System.out.println("get cash cheque details Query : " +
					// pstmt);
					while (rs.next()) {
						totalCash += rs.getDouble("total_cash");
						totalCheque += rs.getDouble("cheque_coll");
						totalDebit += rs.getDouble("debit");
						totalCredit += rs.getDouble("credit");
						totalBounce += rs.getDouble("bounce");
						totalDeposit += rs.getDouble("bank_deposit");
						totalAmount += rs.getDouble("net_amount");
					}
					reportbean = new CashChqReportBean();
					reportbean.setTotalCash(FormatNumber
							.formatNumber(totalCash));
					reportbean.setTotalChq(FormatNumber
							.formatNumber(totalCheque));
					reportbean.setDebit(FormatNumber.formatNumber(totalDebit));
					reportbean
							.setCredit(FormatNumber.formatNumber(totalCredit));
					reportbean.setCheqBounce(FormatNumber
							.formatNumber(totalBounce));
					reportbean.setBankDeposit(FormatNumber
							.formatNumber(totalDeposit));
					reportbean
							.setNetAmt(FormatNumber.formatNumber(totalAmount));
					/*
					 * reportbean.setName(ReportUtility.getOrgNameFromOrgId(con,
					 * agentorgId));
					 */
					reportbean.setName(agtNameOrgIdMap.get(agentorgId));
					reportbean.setOrgId(agentorgId);
					list.add(reportbean);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("get cash cheque details faulty Query : "
					+ pstmt);
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

		if ((LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("ORG_ID")) {
			Collections.sort(list, new Comparator<CashChqReportBean>() {

				@Override
				public int compare(CashChqReportBean o1, CashChqReportBean o2) {
					if (o1.getOrgId() > o2.getOrgId()) {
						return 1;
					} else {
						return -1;
					}

				}
			});

		} else if ((LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("DESC")) {

			Collections.sort(list, new Comparator<CashChqReportBean>() {

				@Override
				public int compare(CashChqReportBean o1, CashChqReportBean o2) {

					return (o2.getName().toUpperCase()).compareTo(o1.getName().toUpperCase());
				}
			});

		} else {
			Collections.sort(list, new Comparator<CashChqReportBean>() {

				@Override
				public int compare(CashChqReportBean o1, CashChqReportBean o2) {

					return (o1.getName().toUpperCase()).compareTo(o2.getName().toUpperCase());
				}
			});

		}
		return list;
	}

	public List<CashChqPmntBean> getCashChqDetailAgentWise(String startDate,
			String endDate, int userId, boolean isExpand, String state,String city) throws LMSException {
		List<CashChqPmntBean> list = new ArrayList<CashChqPmntBean>();
		CashChqPmntBean reportbean = null;
		con = DBConnect.getConnection();
		String appender = "";
		String qry = null;
		Statement stmt = null;
		String cityName = null;
		String condition = null;
		if (!isExpand && userId != -1) {
			condition = " btm.user_id=" + userId
					+ " and ( btm.transaction_date>='" + startDate
					+ "' and btm.transaction_date<='" + endDate
					+ "') and trxtable.transaction_id=btm.transaction_id";
		} else {
			condition = " ( btm.transaction_date>='" + startDate
					+ "' and btm.transaction_date<='" + endDate
					+ "') and trxtable.transaction_id=btm.transaction_id";
		}
		String appendQyr = QueryManager.getAppendOrgOrder();
		String orgCodeQry = " name orgCode ";
		try {
			if (!("-1".equalsIgnoreCase(state))){
				qry = "select state_code from st_lms_state_master where name = '"+state+"';";
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(qry);
				if(rs.next())
					state = rs.getString("state_code");
				appender = " and state_code='" + state + "' ";
				
			}

			if (!("-1".equalsIgnoreCase(city))) {
				qry = "select city_name from st_lms_city_master where city_name='"+ city + "' and state_code='"+state+"';";
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(qry);
				if (rs.next())
					cityName = rs.getString("city_name");
				appender = " and state_code='" + state + "' and city='"+ cityName + "' ";
			}

			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode";

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode ";

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";

			}

			String query = "select data.amount,data.transaction_id,data.type,data.generated_id,data.orgCode,ifnull(bank_name,'-') bankName from st_lms_bo_bank_deposit_transaction tx right join (select amount,transaction_id,type,generated_id,orgCode from ("
					+ "select amount,transaction_id,type,generated_id, "
					+ orgCodeQry
					+ " from (select amount,trx.transaction_id,type,generated_id,transaction_date,trx.party_id from (select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'CASH' type,btm.transaction_date,btm.party_id from st_lms_bo_cash_transaction trxtable, st_lms_bo_transaction_master btm where "
					+ condition
					+ " union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'BANK DEPOSIT' type,btm.transaction_date,btm.party_id from st_lms_bo_bank_deposit_transaction trxtable, st_lms_bo_transaction_master btm where "
					+ condition
					+ " union select ifnull(trxtable.cheque_amt,0) 'amount',btm.transaction_id,'CHEQUE' type,btm.transaction_date,btm.party_id from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type IN ('CHEQUE','CLOSED') and "
					+ condition
					+ " union select ifnull(trxtable.cheque_amt,0) 'amount',btm.transaction_id,'CHQ_BOUNCE' type,btm.transaction_date,btm.party_id from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type='CHQ_BOUNCE' and "
					+ condition
					+ " union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'DEBIT NOTE' type,btm.transaction_date,btm.party_id  from st_lms_bo_debit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='DR_NOTE_CASH' or trxtable.transaction_type ='DR_NOTE') and "
					+ condition
					+ " union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'CREDIT NOTE' type,btm.transaction_date,btm.party_id  from st_lms_bo_credit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='CR_NOTE_CASH' and trxtable.reason not in ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE')) and "
					+ condition
					+ ") trx,st_lms_bo_receipts_trn_mapping brm,st_lms_bo_receipts borec where brm.receipt_id= borec.receipt_id and trx.transaction_id=brm.transaction_id ) innertab ,st_lms_organization_master slom where innertab.party_id = slom.organization_id "+appender+" order by "
					+ appendQyr
					+ ",type) aa )  data on tx.transaction_id=data.transaction_id";

			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			// System.out.println("result set : --------------"+rs);
			// System.out.println("get cash cheque details Query : " + pstmt);
			while (rs.next()) {
				reportbean = new CashChqPmntBean();
				reportbean.setPaymentAmount(rs.getDouble("amount"));
				reportbean.setPaymentType(rs.getString("type"));
				reportbean.setVouncherNo(rs.getString("generated_id"));
				reportbean.setDate(rs.getString("orgCode"));
				reportbean.setBankName(rs.getString("bankName"));
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
				System.out.println("get cash cheque details Query : " + pstmt);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return list;
	}

	/**
	 * @deprecated Use getCashChqDetailDateWise(int orgId) Instead
	 * 
	 */
	public List<CashChqPmntBean> getCashChqDetailDateWise(int orgId, int userId)
			throws LMSException {
		List<CashChqPmntBean> list = new ArrayList<CashChqPmntBean>();
		CashChqPmntBean reportbean = null;
		Connection con = DBConnect.getConnection();
		System.out.println("Agent Date wise report for --> " + orgId);
		String condition = null;
		if (userId != -1) {
			condition = "  trxtable.agent_org_id =" + orgId
					+ " and btm.user_id=" + userId
					+ " and ( btm.transaction_date>='" + startDate
					+ "' and btm.transaction_date<'" + endDate
					+ "') and trxtable.transaction_id=btm.transaction_id ";
		} else {
			condition = " trxtable.agent_org_id =" + orgId
					+ " and ( btm.transaction_date>='" + startDate
					+ "' and btm.transaction_date<'" + endDate
					+ "') and trxtable.transaction_id=btm.transaction_id ";
		}
		String query = "select data.amount,data.transaction_id,data.type,data.generated_id,data.transaction_date,ifnull(bank_name,'-') bankName from st_lms_bo_bank_deposit_transaction tx right join (select amount,trx.transaction_id,type,generated_id,transaction_date from (select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'CASH' type,btm.transaction_date from st_lms_bo_cash_transaction trxtable, st_lms_bo_transaction_master btm where "
				+ condition
				+ " union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'BANK DEPOSIT' type,btm.transaction_date from st_lms_bo_bank_deposit_transaction trxtable, st_lms_bo_transaction_master btm where "
				+ condition
				+ " union select ifnull(trxtable.cheque_amt,0) 'amount',btm.transaction_id,'CHEQUE' type,btm.transaction_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type IN ('CHEQUE','CLOSED') and "
				+ condition
				+ " union select ifnull(trxtable.cheque_amt,0) 'amount',btm.transaction_id,'CHQ_BOUNCE' type,btm.transaction_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type='CHQ_BOUNCE' and "
				+ condition
				+ " union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'DEBIT NOTE' type,btm.transaction_date  from st_lms_bo_debit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='DR_NOTE_CASH' or trxtable.transaction_type ='DR_NOTE') and "
				+ condition
				+ " union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'CREDIT NOTE' type,btm.transaction_date  from st_lms_bo_credit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='CR_NOTE_CASH' and trxtable.reason not in ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE')) and "
				+ condition
				+ ") trx,st_lms_bo_receipts_trn_mapping brm,st_lms_bo_receipts borec where brm.receipt_id= borec.receipt_id and borec.party_id="
				+ orgId
				+ " and trx.transaction_id=brm.transaction_id order by transaction_date asc) data on tx.transaction_id=data.transaction_id";

		try {

			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			// System.out.println("result set : --------------"+rs);
			// System.out.println("get cash cheque details Query : " + pstmt);
			while (rs.next()) {
				reportbean = new CashChqPmntBean();

				reportbean.setPaymentAmount(rs.getDouble("amount"));
				reportbean.setPaymentType(rs.getString("type"));
				reportbean.setVouncherNo(rs.getString("generated_id"));
				reportbean.setDate(rs.getString("transaction_date").replace(
						".0", ""));
				reportbean.setBankName(rs.getString("bankName"));
				list.add(reportbean);
			}
		} catch (SQLException e) {
			System.out.println("get cash cheque details Query : " + pstmt);
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

	public List<CashChqPmntBean> getCashChqDetailAgentWise(int orgId,Map<Integer, List<CashChqPmntBean>> detailDataMap, String stateName, String cityName)
			throws LMSException {

		CashChqPmntBean reportbean = null;
		Connection con = DBConnect.getConnection();
		System.out.println("Agent Date wise report for --> " + orgId);
		String condition = " ";
		String subQry = " ";
		String appenderQuery = "" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if (orgId != -1) {
			condition = "trxtable.agent_org_id =" + orgId
					+ " and ( btm.transaction_date>='" + startDate
					+ "' and btm.transaction_date<'" + endDate
					+ "') and trxtable.transaction_id=btm.transaction_id";
			subQry = "and borec.party_id=" + orgId;
		} else {
			condition = " ( btm.transaction_date>='" + startDate
					+ "' and btm.transaction_date<'" + endDate
					+ "') and trxtable.transaction_id=btm.transaction_id";
		}
		
		if(!("ALL".equalsIgnoreCase(stateName)) || !("ALL".equalsIgnoreCase(cityName))){
		if(!("ALL".equalsIgnoreCase(stateName))) { 
			appenderQuery = appenderQuery.concat(" where om.state_code = '"+stateName+"'");
		}
		if(!("ALL".equalsIgnoreCase(stateName))) {
			appenderQuery = appenderQuery.concat(" and cm.city_name = '"+cityName+"'");
		}
		}
		
		String query = "create temporary table  data  as  select sm.name state_name, cm.city_name city_name, orgId,om."
				+ QueryManager.getOrgCodeQuery()
				+ ",amount,transaction_id,type,generated_id,transaction_date,bankName from ( select data.agent_org_id orgId,data.amount,data.transaction_id,data.type,data.generated_id,data.transaction_date,ifnull(bank_name,'-') bankName from st_lms_bo_bank_deposit_transaction tx right join (select amount,agent_org_id,trx.transaction_id,type,generated_id,transaction_date from (select ifnull(trxtable.amount,0) 'amount',agent_org_id,btm.transaction_id,'CASH' type,btm.transaction_date from st_lms_bo_cash_transaction trxtable, st_lms_bo_transaction_master btm where "
				+ condition
				+ " union select ifnull(trxtable.amount,0) 'amount',agent_org_id,btm.transaction_id,'BANK DEPOSIT' type,btm.transaction_date from st_lms_bo_bank_deposit_transaction trxtable, st_lms_bo_transaction_master btm where "
				+ condition
				+ " union select ifnull(trxtable.cheque_amt,0) 'amount',agent_org_id,btm.transaction_id,'CHEQUE' type,btm.transaction_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type IN ('CHEQUE','CLOSED') and "
				+ condition
				+ " union select ifnull(trxtable.cheque_amt,0) 'amount',agent_org_id,btm.transaction_id,'CHQ_BOUNCE' type,btm.transaction_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type='CHQ_BOUNCE' and "
				+ condition
				+ " union select ifnull(trxtable.amount,0) 'amount',agent_org_id,btm.transaction_id,'DEBIT NOTE' type,btm.transaction_date  from st_lms_bo_debit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='DR_NOTE_CASH' or trxtable.transaction_type ='DR_NOTE') and "
				+ condition
				+ " union select ifnull(trxtable.amount,0) 'amount',agent_org_id,btm.transaction_id,'CREDIT NOTE' type,btm.transaction_date  from st_lms_bo_credit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='CR_NOTE_CASH' and trxtable.reason not in ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE')) and "
				+ condition
				+ ") trx,st_lms_bo_receipts_trn_mapping brm,st_lms_bo_receipts borec where brm.receipt_id= borec.receipt_id "
				+ subQry
				+ " and trx.transaction_id=brm.transaction_id order by transaction_date asc) data on tx.transaction_id=data.transaction_id)main inner join st_lms_organization_master om on om.organization_id=main.orgId " +
						" inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on om.city = cm.city_name "+ appenderQuery+" order by "
				+ QueryManager.getAppendOrgOrder();

		try {
			logger.debug("Aget Wise Cash Collection Query" + query);
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = con.prepareStatement("select * from data ");
			rs = pstmt.executeQuery();
			System.out.println("done");
			// System.out.println("result set : --------------"+rs);
			// System.out.println("get cash cheque details Query : " + pstmt);
			while (rs.next()) {
				reportbean = new CashChqPmntBean();
				reportbean.setOrgName(rs.getString("orgCode"));
				reportbean.setPaymentAmount(rs.getDouble("amount"));
				reportbean.setPaymentType(rs.getString("type"));
				reportbean.setVouncherNo(rs.getString("generated_id"));
				reportbean.setDate(rs.getString("transaction_date").replace(".0", ""));
				reportbean.setBankName(rs.getString("bankName"));
				reportbean.setStateName(rs.getString("state_name"));
				reportbean.setCityName(rs.getString("city_name"));
				int agtorgId = rs.getInt("orgId");
				if (detailDataMap.containsKey(agtorgId)) {
					detailDataMap.get(agtorgId).add(reportbean);
				} else {
					List<CashChqPmntBean> list = new ArrayList<CashChqPmntBean>();
					list.add(reportbean);
					detailDataMap.put(agtorgId, list);

				}
			}
			pstmt = con.prepareStatement("drop table data");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("get cash cheque details Query : " + pstmt);
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

		return null;
	}

	/**
	 * @deprecated use getCashChqDetailDayWise() instead
	 * @date 30DEC2013
	 */
	public List<CashChqReportBean> getCashChqDetailDayWise(int userId,
			boolean isExpand) throws LMSException {
		List<CashChqReportBean> list = new ArrayList<CashChqReportBean>();
		Map<String, CashChqReportBean> repMap = new LinkedHashMap<String, CashChqReportBean>();
		CashChqReportBean reportbean = null;
		String date = null;
		String condition = null;
		con = DBConnect.getConnection();
		System.out.println(startDate + "Day wise report for --> " + endDate);

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		StringBuilder query = new StringBuilder("");
		while (startCal.getTime().before(endCal.getTime())) {
			date = new java.sql.Date(startCal.getTimeInMillis()).toString();
			reportbean = new CashChqReportBean();
			fillReportBean(reportbean);
			reportbean.setName(date);
			repMap.put(date, reportbean);
			startCal.setTimeInMillis(startCal.getTimeInMillis() + 24 * 60 * 60
					* 1000);

		}
		if (!isExpand) {
			condition = "( btm.transaction_date>='"
					+ startDate
					+ "' and btm.transaction_date<'"
					+ endDate
					+ "') and btm.user_id="
					+ userId
					+ " and trxtable.transaction_id=btm.transaction_id group by trx_date";
		} else {
			condition = "( btm.transaction_date>='"
					+ startDate
					+ "' and btm.transaction_date<'"
					+ endDate
					+ "') and trxtable.transaction_id=btm.transaction_id group by trx_date";
		}
		if (LMSFilterDispatcher.isRepFrmSP) {
			query
					.append("select sum(amount) amount,type,transaction_date from (");
		}
		query
				.append("select amount,type,trx_date transaction_date from (select ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'CASH' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from st_lms_bo_cash_transaction trxtable, st_lms_bo_transaction_master btm where "
						+ condition
						+ " union select ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'BANK_DEPOSIT' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from st_lms_bo_bank_deposit_transaction trxtable, st_lms_bo_transaction_master btm where "
						+ condition
						+ " union select ifnull(sum(trxtable.cheque_amt),0) 'amount',btm.transaction_id,'CHEQUE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type IN ('CHEQUE','CLOSED')  and "
						+ condition
						+ " union select ifnull(sum(trxtable.cheque_amt),0) 'amount',btm.transaction_id,'CHQ_BOUNCE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type='CHQ_BOUNCE' and "
						+ condition
						+ " union select ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'DEBIT_NOTE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date  from st_lms_bo_debit_note trxtable, st_lms_bo_transaction_master btm where (trxtable.transaction_type ='DR_NOTE_CASH' or trxtable.transaction_type ='DR_NOTE') and "
						+ condition
						+ " union select ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'CREDIT_NOTE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date  from st_lms_bo_credit_note trxtable, st_lms_bo_transaction_master btm where (trxtable.transaction_type ='CR_NOTE_CASH') and "
						+ condition + ") trx ");
		if (LMSFilterDispatcher.isRepFrmSP) {
			query
					.append("union all select sum(cash_amt),'CASH',finaldate from st_rep_bo_payments where finaldate>='"
							+ startDate
							+ "' and finaldate<'"
							+ endDate
							+ "' group by finaldate"
							+ " union all select sum(credit_note),'CREDIT_NOTE',finaldate from st_rep_bo_payments where finaldate>='"
							+ startDate
							+ "' and finaldate<'"
							+ endDate
							+ "' group by finaldate"
							+ " union all select sum(debit_note),'DEBIT_NOTE',finaldate from st_rep_bo_payments where finaldate>='"
							+ startDate
							+ "' and finaldate<'"
							+ endDate
							+ "' group by finaldate"
							+ " union all select sum(cheque_amt),'CHEQUE',finaldate from st_rep_bo_payments where finaldate>='"
							+ startDate
							+ "' and finaldate<'"
							+ endDate
							+ "' group by finaldate"
							+ " union all select sum(cheque_bounce_amt),'CHQ_BOUNCE',finaldate from st_rep_bo_payments where finaldate>='"
							+ startDate
							+ "' and finaldate<'"
							+ endDate
							+ "' group by finaldate"
							+ " union all select sum(bank_deposit),'BANK_DEPOSIT',finaldate from st_rep_bo_payments where finaldate>='"
							+ startDate
							+ "' and finaldate<'"
							+ endDate
							+ "' group by finaldate) cash group by type,transaction_date");
		}
		try {
			pstmt = con.prepareStatement(query.toString());
			rs = pstmt.executeQuery();
			// System.out.println("result set : --------------"+rs);
			while (rs.next()) {
				reportbean = repMap.get(rs.getString("transaction_date"));
				fillReportBean(reportbean, rs.getString("type"), rs
						.getDouble("amount"));
			}
			list.addAll(repMap.values());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("cash cheque details faulty Query : " + pstmt);
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

	public List<CashChqReportBean> getCashChqDetailUserWise()
			throws LMSException {
		List<CashChqReportBean> list = new ArrayList<CashChqReportBean>();
		CashChqReportBean reportbean = null;
		con = DBConnect.getConnection();
		StringBuilder query = null;
		Map<Integer, String> userMap = new CashChqReportsAgentHelper()
				.getUserList();
		Set<Integer> userIdlist = userMap.keySet();
		double totalCash = 0;
		double totalCheque = 0;
		double totalDebit = 0;
		double totalCredit = 0;
		double totalBounce = 0;
		double totalDeposit = 0;
		double totalAmount = 0;
		double totalPwt = 0;
		try {
		
			HashMap<Integer, String> gameMap = ReportUtility.fetchDrawDataMenu();

			query = new StringBuilder(
					"select sum(total_cash) 'total_cash', sum(credit) 'credit', sum(debit) 'debit', sum(cheque_coll) 'cheque_coll', sum(bounce) 'bounce',sum(bank_deposit) bank_deposit,sum(net_amount) 'net_amount' from (select aa.a 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit', dd.credit_amt 'credit',bd.d 'bank_deposit', ((aa.a+bb.b+dd.credit_amt+bd.d)-(cc.c+gg.debit_amt))'net_amount' from (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and chq.transaction_id=btm.transaction_id ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and chq.transaction_id=btm.transaction_id ) cc,(select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' and bo.reason NOT IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE')) and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) )dd,(select ifnull(sum(bank.amount),0) 'd' from st_lms_bo_bank_deposit_transaction bank, st_lms_bo_transaction_master btm where btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and bank.transaction_id=btm.transaction_id ) bd)) ee");
			for (Integer userId : userIdlist) {
				reportbean = new CashChqReportBean();
				reportbean.setName(userMap.get(userId));
				reportbean.setOrgId(userId);

				pstmt = con.prepareStatement(query.toString());
				pstmt.setInt(1, userId);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);
				pstmt.setInt(4, userId);
				pstmt.setDate(5, startDate);
				pstmt.setDate(6, endDate);
				pstmt.setInt(7, userId);
				pstmt.setDate(8, startDate);
				pstmt.setDate(9, endDate);
				pstmt.setInt(10, userId);
				pstmt.setDate(11, startDate);
				pstmt.setDate(12, endDate);
				pstmt.setInt(13, userId);
				pstmt.setDate(14, startDate);
				pstmt.setDate(15, endDate);
				pstmt.setInt(16, userId);
				pstmt.setDate(17, startDate);
				pstmt.setDate(18, endDate);
				rs = pstmt.executeQuery();
				// System.out.println("result set : --------------"+rs);
				// System.out.println("get cash cheque details Query : " +
				// pstmt);

				double netAmt = 0;
				while (rs.next()) {
					if (rs.getDouble("net_amount") == 0) {
						continue;
					}
					netAmt = rs.getDouble("net_amount");

					totalCash += rs.getDouble("total_cash");
					totalCheque += rs.getDouble("cheque_coll");
					totalDebit += rs.getDouble("debit");
					totalCredit += rs.getDouble("credit");
					totalBounce += rs.getDouble("bounce");
					totalDeposit += rs.getDouble("bank_deposit");
					totalAmount += rs.getDouble("net_amount");
					
					reportbean.setTotalCash(FormatNumber.formatNumber(rs.getDouble("total_cash")));
					reportbean.setTotalChq(FormatNumber.formatNumber(rs.getDouble("cheque_coll")));
					reportbean.setDebit(FormatNumber.formatNumber(rs.getDouble("debit")));
					reportbean.setCredit(FormatNumber.formatNumber(rs.getDouble("credit")));					
					reportbean.setCheqBounce(FormatNumber.formatNumber(rs.getDouble("bounce")));
					reportbean.setBankDeposit(FormatNumber.formatNumber(rs.getDouble("bank_deposit")));
					reportbean.setNetAmt(FormatNumber.formatNumber(rs.getDouble("net_amount")));
				}

				double pwtAmt = 0;
				pstmt = con.prepareStatement("SELECT um.organization_id user_org_id, um.user_id, user_name cashier_name, ticket_nbr, SUM(pwt_amt) pwt_amt, game_name,claimed_date, user_name FROM st_lms_user_master um, (SELECT user_org_id, user_id, ticket_nbr, SUM(pwt_amt) pwt_amt, ? game_name, btm.transaction_date claimed_date FROM st_dg_pwt_inv_?, st_lms_bo_transaction_master btm WHERE btm.transaction_id=bo_transaction_id AND user_id=? AND btm.transaction_date >=? AND btm.transaction_date<=? AND STATUS='CLAIM_PLR_BO' UNION ALL SELECT user_org_id, user_id, ticket_nbr, SUM(pwt_amt) pwt_amt, 'SOCCER' game_name, btm.transaction_date claimed_date FROM st_sle_pwt_inv, st_lms_bo_transaction_master btm WHERE btm.transaction_id=bo_transaction_id AND user_id=? AND btm.transaction_date>=? AND btm.transaction_date<=? AND STATUS='CLAIM_AT_BO') tktTbl WHERE tktTbl.user_id=um.user_id;");
				for (Map.Entry<Integer, String> entry : gameMap.entrySet()) {
					pstmt.setString(1, entry.getValue());
					pstmt.setInt(2, entry.getKey());
					pstmt.setInt(3, userId);
					pstmt.setDate(4, startDate);
					pstmt.setDate(5, endDate);
					pstmt.setInt(6, userId);
					pstmt.setDate(7, startDate);
					pstmt.setDate(8, endDate);
					//pstmt.setInt(9, userId);
					//pstmt.setDate(10, startDate);
					//pstmt.setDate(11, endDate);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						pwtAmt += rs.getDouble("pwt_amt");
						reportbean.setClaimAmt(pwtAmt+"");
						totalPwt += rs.getDouble("pwt_amt");
						totalAmount -= rs.getDouble("pwt_amt");
					}
				}
				
				pstmt = con.prepareStatement("SELECT um.organization_id user_org_id, um.user_id, user_name cashier_name, ticket_nbr, SUM(pwt_amt) pwt_amt, game_name,claimed_date, user_name FROM st_lms_user_master um, ( SELECT user_org_id, user_id, ticket_nbr, SUM(pwt_amt) pwt_amt, 'INSTANT WIN' game_name, btm.transaction_date claimed_date FROM st_iw_pwt_inv, st_lms_bo_transaction_master btm WHERE btm.transaction_id=bo_transaction_id AND user_id=? AND btm.transaction_date>=? AND btm.transaction_date<=? AND STATUS='CLAIM_AT_BO') tktTbl WHERE tktTbl.user_id=um.user_id;");				
				pstmt.setInt(1, userId);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					pwtAmt += rs.getDouble("pwt_amt");
					reportbean.setClaimAmt(pwtAmt+"");
					totalPwt += rs.getDouble("pwt_amt");
					totalAmount -= rs.getDouble("pwt_amt");
				}

				if(netAmt != 0 || pwtAmt != 0) {
					if(netAmt == 0 && pwtAmt != 0) {
						reportbean.setTotalCash("0.00");
						reportbean.setTotalChq("0.00");
						reportbean.setDebit("0.00");
						reportbean.setCredit("0.00");
						reportbean.setCheqBounce("0.00");
						reportbean.setBankDeposit("0.00");
						//reportbean.setNetAmt(FormatNumber.formatNumber(pwtAmt));
					} else if(netAmt != 0 && pwtAmt == 0) {
						reportbean.setClaimAmt("0.00");
					}
					reportbean.setNetAmt(FormatNumber.formatNumber(netAmt-pwtAmt));
					list.add(reportbean);
				}
			}
			reportbean = new CashChqReportBean();
			reportbean.setTotalCash(FormatNumber
					.formatNumber(totalCash));
			reportbean.setTotalChq(FormatNumber
					.formatNumber(totalCheque));
			reportbean.setDebit(FormatNumber.formatNumber(totalDebit));
			reportbean
					.setCredit(FormatNumber.formatNumber(totalCredit));
			reportbean.setCheqBounce(FormatNumber
					.formatNumber(totalBounce));
			reportbean.setBankDeposit(FormatNumber
					.formatNumber(totalDeposit));
			reportbean
					.setNetAmt(FormatNumber.formatNumber(totalAmount));
			reportbean.setTotalClaim(FormatNumber.formatNumber(totalPwt));
			reportbean.setName("-1");
			reportbean.setOrgId(-1);
			list.add(reportbean);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("get cash cheque details faulty Query : " + pstmt);
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

	public List<CashChqReportBean> getCashChqDetailUserAgentWise(
			List<Integer> agtidlist, int userId) throws LMSException {
		List<CashChqReportBean> list = new ArrayList<CashChqReportBean>();
		CashChqReportBean reportbean = null;
		con = DBConnect.getConnection();
		StringBuilder query = null;
		Map<Integer, String> agtNameOrgIdMap = new LinkedHashMap<Integer, String>();
		try {
			if (agtidlist.size() > 0) {
				ReportUtility.getOrgNameMap(con, agtNameOrgIdMap, agtidlist
						.toString().replace("[", " ").replace("]", " "));
			}
			query = new StringBuilder(
					"select sum(total_cash) 'total_cash', sum(credit) 'credit', sum(debit) 'debit', sum(cheque_coll) 'cheque_coll', sum(bounce) 'bounce',sum(bank_deposit) bank_deposit,sum(net_amount) 'net_amount' from (select aa.a 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit', dd.credit_amt 'credit',bd.d 'bank_deposit', ((aa.a+bb.b+dd.credit_amt+bd.d)-(cc.c+gg.debit_amt))'net_amount' from (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and chq.transaction_id=btm.transaction_id ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and chq.transaction_id=btm.transaction_id ) cc,(select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' and bo.reason not in ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE')) and agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) )dd,(select ifnull(sum(bank.amount),0) 'd' from st_lms_bo_bank_deposit_transaction bank, st_lms_bo_transaction_master btm where bank.agent_org_id = ? and btm.user_id= ? and ( btm.transaction_date >= ? and btm.transaction_date <= ?) and bank.transaction_id=btm.transaction_id ) bd)) ee");
			/*
			 * if(LMSFilterDispatcher.isRepFrmSP){query.append(
			 * " union all select sum(cash_amt) 'total_cash', sum(cheque_amt) 'cheque_coll', sum(cheque_bounce_amt) 'bounce', sum(debit_note) 'debit', sum(credit_note) 'credit',sum(bank_deposit) 'bank_deposit',sum(((cash_amt+cheque_amt+credit_note+bank_deposit)-(cheque_bounce_amt+debit_note))) as 'net_amount' from st_rep_bo_payments where agent_org_id = ? and finaldate >= ? and finaldate <= ?"
			 * ); }
			 */
			for (Integer agentorgId : agtidlist) {
				double totalCash = 0;
				double totalCheque = 0;
				double totalDebit = 0;
				double totalCredit = 0;
				double totalBounce = 0;
				double totalDeposit = 0;
				double totalAmount = 0;
				pstmt = con.prepareStatement(query.toString());
				pstmt.setInt(1, agentorgId);
				pstmt.setInt(2, userId);
				pstmt.setDate(3, startDate);
				pstmt.setDate(4, endDate);
				pstmt.setInt(5, agentorgId);
				pstmt.setInt(6, userId);
				pstmt.setDate(7, startDate);
				pstmt.setDate(8, endDate);
				pstmt.setInt(9, agentorgId);
				pstmt.setInt(10, userId);
				pstmt.setDate(11, startDate);
				pstmt.setDate(12, endDate);
				pstmt.setInt(13, agentorgId);
				pstmt.setInt(14, userId);
				pstmt.setDate(15, startDate);
				pstmt.setDate(16, endDate);
				pstmt.setInt(17, agentorgId);
				pstmt.setInt(18, userId);
				pstmt.setDate(19, startDate);
				pstmt.setDate(20, endDate);
				pstmt.setInt(21, agentorgId);
				pstmt.setInt(22, userId);
				pstmt.setDate(23, startDate);
				pstmt.setDate(24, endDate);
				/*
				 * if(LMSFilterDispatcher.isRepFrmSP){ pstmt.setInt(25,
				 * agentorgId); pstmt.setDate(26, startDate); pstmt.setDate(27,
				 * endDate); }
				 */
				rs = pstmt.executeQuery();
				while (rs.next()) {
					totalCash += rs.getDouble("total_cash");
					totalCheque += rs.getDouble("cheque_coll");
					totalDebit += rs.getDouble("debit");
					totalCredit += rs.getDouble("credit");
					totalBounce += rs.getDouble("bounce");
					totalDeposit += rs.getDouble("bank_deposit");
					totalAmount += rs.getDouble("net_amount");
				}
				reportbean = new CashChqReportBean();
				reportbean.setTotalCash(FormatNumber.formatNumber(totalCash));
				reportbean.setTotalChq(FormatNumber.formatNumber(totalCheque));
				reportbean.setDebit(FormatNumber.formatNumber(totalDebit));
				reportbean.setCredit(FormatNumber.formatNumber(totalCredit));
				reportbean
						.setCheqBounce(FormatNumber.formatNumber(totalBounce));
				reportbean.setBankDeposit(FormatNumber
						.formatNumber(totalDeposit));
				reportbean.setNetAmt(FormatNumber.formatNumber(totalAmount));
				reportbean.setName(agtNameOrgIdMap.get(agentorgId.intValue()));
				reportbean.setOrgId(agentorgId);
				list.add(reportbean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("get cash cheque details faulty Query : "
					+ pstmt);
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
		if ((LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("ORG_ID")) {
			Collections.sort(list, new Comparator<CashChqReportBean>() {

				@Override
				public int compare(CashChqReportBean o1, CashChqReportBean o2) {
					if (o1.getOrgId() > o2.getOrgId()) {
						return 1;
					} else {
						return -1;
					}

				}
			});

		} else if ((LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("DESC")) {

			Collections.sort(list, new Comparator<CashChqReportBean>() {

				@Override
				public int compare(CashChqReportBean o1, CashChqReportBean o2) {

					return (o2.getName().toUpperCase()).compareTo(o1.getName().toUpperCase());
				}
			});

		} else {
			Collections.sort(list, new Comparator<CashChqReportBean>() {

				@Override
				public int compare(CashChqReportBean o1, CashChqReportBean o2) {

					return (o1.getName().toUpperCase()).compareTo(o2.getName().toUpperCase());
				}
			});

		}

		return list;
	}

	public List<CashierDrawerDataForPWTBean> getDetailsForPwtTicketsCashierWise(
			DateBeans dateBeans, String cashierType, int userId)
			throws LMSException, ParseException, WriteException {

		int diffInDays = 0;
		int maxLimitDays = 7;
		int oneDay = 1000 * 60 * 60 * 24;

		StringBuilder query = null;

		Timestamp startDate = null;
		Timestamp endDate = null;
		List<CashierDrawerDataForPWTBean> detailsForPwtTicketsCashierWise = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*
		cashierType = "ALL".equals(cashierType) ? "" : " and user_id= "
				+ userId;
*/
		diffInDays = (int) ((dateBeans.getEndDate().getTime() - dateBeans
				.getStartDate().getTime()) / oneDay);
		if (diffInDays > maxLimitDays) {
			startDate = new Timestamp(sdf
					.parse(
							new Timestamp(dateBeans.getEndDate().getTime()
									- oneDay * 6).toString().substring(0, 10))
					.getTime());
			endDate = new Timestamp(sdf.parse(new Timestamp(dateBeans.getEndDate().getTime()).toString().substring(0, 10)).getTime()
					+ oneDay - 1000);
			dateBeans.setStartDate(new java.util.Date(dateBeans.getEndDate().getTime()- oneDay* 6));

			new WriteExcelForCashChq(dateBeans);

		} else {
			startDate = new Timestamp(sdf.parse(
					new Timestamp(dateBeans.getStartDate().getTime())
							.toString().substring(0, 10)).getTime());
			endDate = new Timestamp(sdf.parse(
					new Timestamp(dateBeans.getEndDate().getTime()).toString()
							.substring(0, 10)).getTime()
					+ oneDay - 1000);
		}

		return getCashierWisewtData(startDate, endDate, cashierType, userId);
	}

	public List<CashierDrawerDataForPWTBean> getCashierWisewtData(
			Timestamp startDate, Timestamp endDate, String cashierType,
			int userId) throws LMSException {
		List<CashierDrawerDataForPWTBean> detailsForPwtTicketsCashierWise = null;
		con = DBConnect.getConnection();
		cashierType = "ALL".equals(cashierType) ? "" : " and user_id= "
				+ userId;
		HashMap<Integer, String> gameMap = ReportUtility.fetchDrawDataMenu();
		Map<Integer, String> iwGameMap = ReportUtility.getIWGameMap(startDate);
		StringBuilder query = new StringBuilder("");
		try {
			for (Map.Entry<Integer, String> entry : gameMap.entrySet()) {
				query
						.append(
								"select um.organization_id user_org_id, um.user_id,user_name cashier_name,ticket_nbr, pwt_amt, game_name,claimed_date, user_name from st_lms_user_master um,(select user_org_id, user_id,ticket_nbr,sum(pwt_amt) pwt_amt, '"
										+ entry.getValue()
										+ "' game_name,btm.transaction_date claimed_date from st_dg_pwt_inv_"
										+ entry.getKey()
										+ " ,st_lms_bo_transaction_master btm where  btm.transaction_id=bo_transaction_id  and ( btm.transaction_date >= '"
										+ startDate
										+ "' and btm.transaction_date <= '"
										+ endDate
										+ "') and status='CLAIM_PLR_BO' "
										+ cashierType
										+ " group by  ticket_nbr order by ticket_nbr) tktTbl where tktTbl.user_id=um.user_id")
						.append(" union ");
			}
			for (Map.Entry<Integer, String> entry : iwGameMap.entrySet()) {
				query
						.append(
								"select um.organization_id user_org_id, um.user_id,user_name cashier_name,ticket_nbr, pwt_amt, game_name,claimed_date, user_name from st_lms_user_master um,(select user_org_id, user_id,ticket_nbr,sum(pwt_amt) pwt_amt, '"
										+ entry.getValue()
										+ "' game_name,btm.transaction_date claimed_date from st_iw_pwt_inv"
										+ " ,st_lms_bo_transaction_master btm where  btm.transaction_id=bo_transaction_id  and ( btm.transaction_date >= '"
										+ startDate
										+ "' and btm.transaction_date <= '"
										+ endDate
										+ "') and status='CLAIM_AT_BO' "
										+ cashierType
										+ " group by  ticket_nbr order by ticket_nbr) tktTbl where tktTbl.user_id=um.user_id")
						.append(" union ");
			}

			pstmt = con.prepareStatement(query.replace(
					query.lastIndexOf("union"), query.length(), "").append(
					" order by user_id").toString());
			rs = pstmt.executeQuery();

			detailsForPwtTicketsCashierWise = new ArrayList<CashierDrawerDataForPWTBean>();

			while (rs.next()) {
				CashierDrawerDataForPWTBean cashierDrawerDataForPWTBean = new CashierDrawerDataForPWTBean();
				String ticketNumber = rs.getString("ticket_nbr");
				
				int tktLen = ticketNumber.length();
				cashierDrawerDataForPWTBean.setCashierName(rs
						.getString("cashier_name"));
				cashierDrawerDataForPWTBean.setUserId(rs.getInt("user_id"));
				
				if("INSTANT WIN".equalsIgnoreCase(rs.getString("game_name"))){
					cashierDrawerDataForPWTBean.setTicketNumber(ticketNumber);
				}else{
				String tktFormat = Util.getTicketNumberFormat(ticketNumber
						+ "0");
				if (tktFormat.equals("OLDTKTFORMAT")) {
					cashierDrawerDataForPWTBean.setTicketNumber(ticketNumber
							.substring(0, tktLen - 4).concat("XXXX"));
				} else {
					cashierDrawerDataForPWTBean.setTicketNumber(ticketNumber
							.replace((ticketNumber.substring(0, 4)), "XXXX"));
				}}

				cashierDrawerDataForPWTBean.setGameName(rs
						.getString("game_name"));
				cashierDrawerDataForPWTBean.setClaimedTime(rs
						.getTimestamp("claimed_date"));
				cashierDrawerDataForPWTBean.setClaimedAmount(rs
						.getDouble("pwt_amt"));
				detailsForPwtTicketsCashierWise
						.add(cashierDrawerDataForPWTBean);
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

		return detailsForPwtTicketsCashierWise;
	}

	public List<CashChqReportBean> getCashChqDetail(String state, String city)
			throws LMSException {
		List<CashChqReportBean> list = new ArrayList<CashChqReportBean>();
		CashChqReportBean reportbean = null;
		StringBuilder query = null;
		Statement stmt = null;
		String qry;
		double totalCash = 0;
		double totalCheque = 0;
		double totalDebit = 0;
		double totalCredit = 0;
		double totalBounce = 0;
		double totalDeposit = 0;
		double totalAmount = 0;
		String cityName = null;
		String appender = "";
		String orgCodeQry = null;
		try {
			boolean isArchData = ReportUtility.isArchData(startDate);
			con = DBConnect.getConnection();
			if (!("-1".equalsIgnoreCase(state)))
				appender = " and sm.state_code='" + state + "' ";

			if (!("-1".equalsIgnoreCase(city))) {
				qry = "select city_name from st_lms_city_master where city_code='"
						+ city + "' and state_code='" + state + "';";
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(qry);
				if (rs.next())
					cityName = rs.getString("city_name");
				appender = " and slom.state_code='" + state + "' and city='"
						+ cityName + "' ";
			}
			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " slom.org_code orgCode ";
			} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',slom.name)  orgCode  ";
			} else if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(slom.name,'_',org_code)  orgCode  ";
			} else{
				orgCodeQry = " slom.name orgCode ";
			}

			query = new StringBuilder(
					"select state_name, city_name, sum(total_cash) total_cash,sum(cheque_coll) cheque_coll,sum(bounce)  bounce,sum(debit) debit,sum(credit) credit,sum(bank_deposit) bank_deposit, sum(net_amount) net_amount,orgCode,organization_id  from ( select state_name, city_name, ifnull(aa.a,0) 'total_cash',ifnull( bb.b,0) 'cheque_coll',ifnull( cc.c,0) 'bounce' ,ifnull( gg.debit_amt,0) 'debit',ifnull( dd.credit_amt,0) 'credit',ifnull(bd.d,0) 'bank_deposit', ((ifnull(aa.a,0)+ifnull( bb.b,0)+ifnull( dd.credit_amt,0)+ifnull(bd.d,0))-(ifnull( cc.c,0)+ifnull( gg.debit_amt,0)))'net_amount',orgCode,main.organization_id from  (select state_name, city_name, om.orgCode,om.organization_id from (select sm.name state_name, city city_name,"
							+ orgCodeQry
							+ ",organization_id from st_lms_organization_master slom inner join st_lms_state_master sm on slom.state_code =sm.state_code  where organization_type='AGENT' "
							+ appender
							+ ")om  left join (select organization_id,registration_date,termination_date from st_lms_user_master  where (termination_date < ? or registration_date>= ? ) and isrolehead='Y' and organization_type='AGENT')um  on um.organization_id=om.organization_id)main  left join (select ifnull(sum(cash.amount),0) 'a',cash.agent_org_id  from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where ( btm.transaction_date >=? and btm.transaction_date < ?) and cash.transaction_id=btm.transaction_id group by  cash.agent_org_id ) aa   on main.organization_id=aa.agent_org_id    left join ( select ifnull(sum(chq.cheque_amt),0) 'b',chq.agent_org_id from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and ( btm.transaction_date >= ? and btm.transaction_date < ?) and chq.transaction_id=btm.transaction_id group by  chq.agent_org_id ) bb    on main.organization_id=bb.agent_org_id    left join  ( select ifnull(sum(chq.cheque_amt),0) 'c',chq.agent_org_id from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE'  and ( btm.transaction_date >= ? and btm.transaction_date < ?) and chq.transaction_id=btm.transaction_id  group by  chq.agent_org_id ) cc   on main.organization_id=cc.agent_org_id   left join  (select ifnull(sum(bo.amount),0) 'debit_amt',agent_org_id  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE')  and ( btm.transaction_date >= ? and btm.transaction_date < ?) group by  agent_org_id  )gg   on main.organization_id=gg.agent_org_id left join (select ifnull(sum(bo.amount),0) 'credit_amt' ,agent_org_id from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' and bo.reason NOT IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE')) and ( btm.transaction_date >= ? and btm.transaction_date < ?)  group by  agent_org_id )dd on main.organization_id=dd.agent_org_id left join (select ifnull(sum(bank.amount),0) 'd',bank.agent_org_id from st_lms_bo_bank_deposit_transaction bank, st_lms_bo_transaction_master btm where  ( btm.transaction_date >= ? and btm.transaction_date < ?) and bank.transaction_id=btm.transaction_id  group by  agent_org_id ) bd on main.organization_id= bd.agent_org_id  ");
			if (isArchData) {

				query
						.append(" union all select state_name, city_name, ifnull(total_cash,0),ifnull(cheque_coll,0), ifnull(bounce,0),ifnull(debit,0), ifnull(credit,0),   ifnull(bank_deposit,0), ifnull(net_amount,0),orgCode,organization_id from (select state_name, city_name, om.orgCode,om.organization_id from (select sm.name state_name, city city_name,"
								+ orgCodeQry
								+ ",organization_id from st_lms_organization_master  slom inner join st_lms_state_master sm on slom.state_code =sm.state_code where organization_type='AGENT' "
								+ appender
								+ ")om  left join (select organization_id,registration_date,termination_date from st_lms_user_master  where (termination_date < ? or registration_date>= ? ) and isrolehead='Y' and organization_type='AGENT')um  on um.organization_id=om.organization_id)main  left join (select sum(cash_amt) 'total_cash',  sum(credit_note) 'credit', sum(debit_note) 'debit', sum(cheque_amt) 'cheque_coll', sum(cheque_bounce_amt) 'bounce', sum(bank_deposit) 'bank_deposit',sum(((cash_amt+cheque_amt+credit_note+bank_deposit)-(cheque_bounce_amt+debit_note))) as 'net_amount',agent_org_id from st_rep_bo_payments where  finaldate >= ?  and finaldate <?  group by  agent_org_id)rep  on main.organization_id= rep.agent_org_id");
			}
			query.append(" )final  group by  organization_id  order by "
					+ QueryManager.getAppendOrgOrder());
			totalCash = 0;
			totalCheque = 0;
			totalDebit = 0;
			totalCredit = 0;
			totalBounce = 0;
			totalDeposit = 0;
			totalAmount = 0;
			pstmt = con.prepareStatement(query.toString());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			pstmt.setDate(5, startDate);
			pstmt.setDate(6, endDate);
			pstmt.setDate(7, startDate);
			pstmt.setDate(8, endDate);
			pstmt.setDate(9, startDate);
			pstmt.setDate(10, endDate);
			pstmt.setDate(11, startDate);
			pstmt.setDate(12, endDate);
			pstmt.setDate(13, startDate);
			pstmt.setDate(14, endDate);
			if (isArchData) {
				pstmt.setDate(15, startDate);
				pstmt.setDate(16, endDate);
				pstmt.setDate(17, startDate);
				pstmt.setDate(18, endDate);
			}
			logger.info("get cash cheque details  Query : " + pstmt);
			rs = pstmt.executeQuery();
			// System.out.println("result set : --------------"+rs);
			// System.out.println("get cash cheque details Query : " +
			// pstmt);
			while (rs.next()) {
				totalCash += rs.getDouble("total_cash");
				totalCheque += rs.getDouble("cheque_coll");
				totalDebit += rs.getDouble("debit");
				totalCredit += rs.getDouble("credit");
				totalBounce += rs.getDouble("bounce");
				totalDeposit += rs.getDouble("bank_deposit");
				totalAmount += rs.getDouble("net_amount");

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

				reportbean.setName(rs.getString("orgCode"));
				reportbean.setOrgId(rs.getInt("organization_id"));
				reportbean.setState(rs.getString("state_name"));
				reportbean.setCity(rs.getString("city_name"));
				list.add(reportbean);

			}
			reportbean = new CashChqReportBean();
			reportbean.setTotalCash(FormatNumber.formatNumber(totalCash));
			reportbean.setTotalChq(FormatNumber.formatNumber(totalCheque));
			reportbean.setDebit(FormatNumber.formatNumber(totalDebit));
			reportbean.setCredit(FormatNumber.formatNumber(totalCredit));
			reportbean.setCheqBounce(FormatNumber.formatNumber(totalBounce));
			reportbean.setBankDeposit(FormatNumber.formatNumber(totalDeposit));
			reportbean.setNetAmt(FormatNumber.formatNumber(totalAmount));
			reportbean.setName("-1");
			reportbean.setOrgId(-1);
			list.add(reportbean);

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

	public List<CashChqReportBean> getCashChqDetailDayWise(String state,
			String city) throws LMSException {
		List<CashChqReportBean> list = new ArrayList<CashChqReportBean>();
		Map<String, CashChqReportBean> repMap = new LinkedHashMap<String, CashChqReportBean>();
		CashChqReportBean reportbean = null;
		String date = null;
		double totalCash = 0;
		double totalCheque = 0;
		double totalDebit = 0;
		double totalCredit = 0;
		double totalBounce = 0;
		double totalDeposit = 0;
		double totalAmount = 0;
		String qry = null;
		Statement stmt = null;
		String appender = "";
		String cityName = null;
		String type = "";
		con = DBConnect.getConnection();
		System.out.println(startDate + "Day wise report for --> " + endDate);
		boolean isArchData = ReportUtility.isArchData(startDate);
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		StringBuilder query = new StringBuilder("");
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

			if (!("-1".equalsIgnoreCase(state)))
				appender = " where state_code='" + state + "' ";

			if (!("-1".equalsIgnoreCase(city))) {
				qry = "select city_name from st_lms_city_master where city_code='"
						+ city + "' and state_code='" + state + "';";
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(qry);
				if (rs.next())
					cityName = rs.getString("city_name");
				appender = " where state_code='" + state + "' and city='"
						+ cityName + "' ";
			}

			if (isArchData) {
				query
						.append("select sum(amount) amount,type,transaction_date from (");
			}
			query
					.append("select sum(amount) amount,type, transaction_date from (select amount,type,trx_date transaction_date from (select party_id,ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'CASH' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from st_lms_bo_cash_transaction trxtable, st_lms_bo_transaction_master btm where  ( btm.transaction_date>=? and btm.transaction_date<?) and trxtable.transaction_id=btm.transaction_id group by trx_date,party_id  union select party_id, ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'BANK_DEPOSIT' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from st_lms_bo_bank_deposit_transaction trxtable, st_lms_bo_transaction_master btm where  ( btm.transaction_date>=? and btm.transaction_date<?) and trxtable.transaction_id=btm.transaction_id group by trx_date,party_id  union select party_id,ifnull(sum(trxtable.cheque_amt),0) 'amount',btm.transaction_id,'CHEQUE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type IN ('CHEQUE','CLOSED')  and  ( btm.transaction_date>=? and btm.transaction_date<?) and trxtable.transaction_id=btm.transaction_id group by trx_date,party_id  union select party_id,ifnull(sum(trxtable.cheque_amt),0) 'amount',btm.transaction_id,'CHQ_BOUNCE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type='CHQ_BOUNCE' and ( btm.transaction_date>=? and btm.transaction_date<?) and trxtable.transaction_id=btm.transaction_id group by trx_date,party_id  union select party_id,ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'DEBIT_NOTE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date  from st_lms_bo_debit_note trxtable, st_lms_bo_transaction_master btm where (trxtable.transaction_type ='DR_NOTE_CASH' or trxtable.transaction_type ='DR_NOTE') and  ( btm.transaction_date>=? and btm.transaction_date<?) and trxtable.transaction_id=btm.transaction_id group by trx_date,party_id  union select party_id,ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'CREDIT_NOTE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date  from st_lms_bo_credit_note trxtable, st_lms_bo_transaction_master btm where (trxtable.transaction_type ='CR_NOTE_CASH' and trxtable.reason NOT IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE')) and  ( btm.transaction_date>=? and btm.transaction_date<?) and trxtable.transaction_id=btm.transaction_id group by trx_date,party_id ) trx inner join st_lms_organization_master om on om.organization_id=trx.party_id"
							+ appender
							+ " )fg group by transaction_date, type ");
			if (isArchData) {
				query
						.append(" union all select sum(amt) as amount,CASH as type,finaldate from (select agent_org_id,sum(cash_amt) amt,'CASH',finaldate from st_rep_bo_payments where finaldate>=? and finaldate< ? group by finaldate,agent_org_id  union all select agent_org_id,sum(credit_note) amt,'CREDIT_NOTE',finaldate from st_rep_bo_payments where finaldate>=? and finaldate<? group by finaldate,agent_org_id union all select agent_org_id,sum(debit_note) amt,'DEBIT_NOTE',finaldate from st_rep_bo_payments where finaldate>=? and finaldate<? group by finaldate,agent_org_id  union all select agent_org_id,sum(cheque_amt) amt,'CHEQUE',finaldate from st_rep_bo_payments where finaldate>=? and finaldate<? group by finaldate,agent_org_id  union all select agent_org_id,sum(cheque_bounce_amt) amt,'CHQ_BOUNCE',finaldate from st_rep_bo_payments where finaldate>=? and finaldate<? group by finaldate,agent_org_id  union all select agent_org_id,sum(bank_deposit) amt,'BANK_DEPOSIT',finaldate from st_rep_bo_payments where finaldate>=? and finaldate<? group by finaldate,agent_org_id)fk inner join st_lms_organization_master om on om.organization_id=fk.agent_org_id "
								+ appender
								+ " group by finaldate,CASH) cash group by type,transaction_date");
			}

			pstmt = con.prepareStatement(query.toString());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			pstmt.setDate(5, startDate);
			pstmt.setDate(6, endDate);
			pstmt.setDate(7, startDate);
			pstmt.setDate(8, endDate);
			pstmt.setDate(9, startDate);
			pstmt.setDate(10, endDate);
			pstmt.setDate(11, startDate);
			pstmt.setDate(12, endDate);
			if (isArchData) {
				pstmt.setDate(13, startDate);
				pstmt.setDate(14, endDate);
				pstmt.setDate(15, startDate);
				pstmt.setDate(16, endDate);
				pstmt.setDate(17, startDate);
				pstmt.setDate(18, endDate);
				pstmt.setDate(19, startDate);
				pstmt.setDate(20, endDate);
				pstmt.setDate(21, startDate);
				pstmt.setDate(22, endDate);
				pstmt.setDate(23, startDate);
				pstmt.setDate(24, endDate);
			}

			rs = pstmt.executeQuery();
			logger.info("getCashChqDetailDayWise query" + pstmt);
			// System.out.println("result set : --------------"+rs);
			while (rs.next()) {
				reportbean = repMap.get(rs.getString("transaction_date"));
				fillReportBean(reportbean, rs.getString("type"), rs
						.getDouble("amount"));
				type = rs.getString("type");

				reportbean
						.setNetAmt(FormatNumber
								.formatNumber((Double.parseDouble(reportbean
										.getTotalCash())
										+ Double.parseDouble(reportbean
												.getTotalChq())
										+ Double.parseDouble(reportbean
												.getCredit()) + Double
										.parseDouble(reportbean
												.getBankDeposit()))
										- (Double.parseDouble(reportbean
												.getCheqBounce()) + Double
												.parseDouble(reportbean
														.getDebit()))));
			}

			// totalAmount += Double.parseDouble(reportbean.getNetAmt());
			list.addAll(repMap.values());

			for (Map.Entry<String, CashChqReportBean> mapItr : repMap
					.entrySet()) {
				totalCash += Double.parseDouble(mapItr.getValue()
						.getTotalCash());
				totalCheque += Double.parseDouble(mapItr.getValue()
						.getTotalChq());
				totalDebit += Double.parseDouble(mapItr.getValue().getDebit());
				totalCredit += Double
						.parseDouble(mapItr.getValue().getCredit());
				totalBounce += Double.parseDouble(mapItr.getValue()
						.getCheqBounce());
				totalDeposit += Double.parseDouble(mapItr.getValue()
						.getBankDeposit());
				totalAmount += Double
						.parseDouble(mapItr.getValue().getNetAmt());
			}

			reportbean = new CashChqReportBean();
			reportbean.setTotalCash(FormatNumber.formatNumber(totalCash));
			reportbean.setTotalChq(FormatNumber.formatNumber(totalCheque));
			reportbean.setDebit(FormatNumber.formatNumber(totalDebit));
			reportbean.setCredit(FormatNumber.formatNumber(totalCredit));
			reportbean.setCheqBounce(FormatNumber.formatNumber(totalBounce));
			reportbean.setBankDeposit(FormatNumber.formatNumber(totalDeposit));
			reportbean.setNetAmt(FormatNumber.formatNumber(totalAmount));
			reportbean.setName("-1");
			reportbean.setOrgId(-1);
			list.add(reportbean);

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("cash cheque details faulty Query : " + pstmt);
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