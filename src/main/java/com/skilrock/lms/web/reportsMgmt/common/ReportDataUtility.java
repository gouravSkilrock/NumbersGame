package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.PaymentReportBean;
import com.skilrock.lms.beans.PaymentRequestBean;
import com.skilrock.lms.common.exception.LMSException;

public class ReportDataUtility {
	static Log logger = LogFactory.getLog(ReportDataUtility.class);

	public static void getDGTransaction(PaymentRequestBean requestBean, Map<Integer, PaymentReportBean> paymentReportMap, Connection con) {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String saleQuery = null;
		String cancelQuery = null;
		String pwtQuery = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT game_id FROM st_dg_game_master;");
			while(rs.next()) {
				int gameId = rs.getInt("game_id");
				saleQuery += "(SELECT drs.retailer_org_id, SUM(net_amt) AS sale FROM st_dg_ret_sale_"+gameId+" drs INNER JOIN st_lms_retailer_transaction_master rtm ON drs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_SALE','DG_SALE_OFFLINE') AND transaction_date>="+requestBean.getStartDate()+" AND transaction_date<="+requestBean.getEndDate()+" AND rtm.retailer_org_id="+requestBean.getRetailerOrgId()+" GROUP BY rtm.retailer_org_id) UNION ALL ";
				cancelQuery += "(SELECT drs.retailer_org_id, SUM(net_amt) AS cancel FROM st_dg_ret_sale_refund_"+gameId+" drs INNER JOIN st_lms_retailer_transaction_master rtm ON drs.transaction_id=rtm.transaction_id WHERE transaction_type IN ('DG_REFUND_CANCEL','DG_REFUND_FAILED') AND transaction_date>="+requestBean.getStartDate()+" AND transaction_date<="+requestBean.getEndDate()+" AND rtm.retailer_org_id="+requestBean.getRetailerOrgId()+" GROUP BY rtm.retailer_org_id) UNION ALL ";
				pwtQuery += "(SELECT drs.retailer_org_id, SUM(pwt_amt+retailer_claim_comm) AS pwt FROM st_dg_ret_pwt_"+gameId+" drs INNER JOIN st_lms_retailer_transaction_master rtm ON drs.transaction_id=rtm.transaction_id WHERE transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') AND transaction_date>="+requestBean.getStartDate()+" AND transaction_date<="+requestBean.getEndDate()+" AND rtm.retailer_org_id="+requestBean.getRetailerOrgId()+" GROUP BY rtm.retailer_org_id) UNION ALL ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getNetPaymentTransactionAgent(PaymentRequestBean requestBean, Map<Integer, PaymentReportBean> paymentReportMap, Connection con) {

		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String cashQuery = "";
		String chequeQuery = "";
		String chequeBounceQuery = "";
		String debitQuery = "";
		String creditQuery = "";
		String bankDepositQuery = "";
		String appender = null;
		String footer = null;
		PaymentReportBean paymentBean = null;
		try {
			if(!(requestBean.isCashReq() || requestBean.isChequeReq() || requestBean.isChequeBounceReq() || requestBean.isDebitReq() || requestBean.isCreditReq() || requestBean.isBankDepositReq()))
				throw new LMSException("Please Select Atleast one Transaction Type.");

			query = "SELECT organization_id agentOrgId, NAME agentName, IFNULL(SUM(cash),0.0) 'cash', IFNULL(SUM(cheque),0.0) 'cheque', IFNULL(SUM(cheque_bounce),0.0) 'cheque_bounce', IFNULL(SUM(debit),0.0) 'debit', IFNULL(SUM(credit),0.0) 'credit', IFNULL(SUM(bank_deposit),0.0) 'bank_deposit', (cash+cheque-cheque_bounce-debit+credit+bank_deposit) AS net_payment FROM ( SELECT agent_org_id, SUM(cash) 'cash', SUM(cheque) 'cheque', SUM(cheque_bounce) 'cheque_bounce', SUM(debit) 'debit', SUM(credit) 'credit', SUM(bank_deposit) 'bank_deposit' FROM (";
			if(requestBean.getAgentOrgId() == 0) {
				String joinType = null;
				if(requestBean.isAllDataReq()) {
					joinType = "RIGHT";
				} else {
					joinType = "INNER";
				}

				appender = "AND transaction_date>='"+requestBean.getStartDate()+"' AND transaction_date<='"+requestBean.getEndDate()+"' GROUP BY agent_org_id";
				footer = ")aa GROUP BY agent_org_id)bb "+joinType+" JOIN (SELECT organization_id, NAME FROM st_lms_organization_master slom WHERE organization_type='AGENT')slom ON bb.agent_org_id=slom.organization_id GROUP BY slom.organization_id;";
			} else {
				appender = "AND transaction_date>='"+requestBean.getStartDate()+"' AND transaction_date<='"+requestBean.getEndDate()+"' AND agent_org_id="+requestBean.getAgentOrgId();
				footer = ")aa)bb INNER JOIN st_lms_organization_master ON agent_org_id=organization_id;";
			}

			if(requestBean.isCashReq()) {
				cashQuery = "SELECT agent_org_id, IFNULL(SUM(amount),0) 'cash', 0.0 'cheque', 0.0 'cheque_bounce', 0.0 'debit', 0.0 'credit', 0.0 'bank_deposit' FROM st_lms_bo_cash_transaction cash INNER JOIN st_lms_bo_transaction_master btm ON cash.transaction_id=btm.transaction_id "+appender+" UNION ALL ";
			}
			if(requestBean.isChequeReq()) {
				chequeQuery = "SELECT agent_org_id, 0.0 'cash', IFNULL(SUM(cheque_amt),0.0) 'cheque', 0.0 'cheque_bounce', 0.0 'debit', 0.0 'credit', 0.0 'bank_deposit' FROM st_lms_bo_sale_chq chq INNER JOIN st_lms_bo_transaction_master btm ON chq.transaction_id=btm.transaction_id AND chq.transaction_type IN ('CHEQUE','CLOSED') "+appender+" UNION ALL ";
			}
			if(requestBean.isChequeBounceReq()) {
				chequeBounceQuery = "SELECT agent_org_id, 0.0 'cash', 0.0 'cheque', IFNULL(SUM(cheque_amt),0.0) 'cheque_bounce', 0.0 'debit', 0.0 'credit', 0.0 'bank_deposit' FROM st_lms_bo_sale_chq chq INNER JOIN st_lms_bo_transaction_master btm ON chq.transaction_id=btm.transaction_id AND chq.transaction_type IN ('CHQ_BOUNCE') "+appender+" UNION ALL ";
			}
			if(requestBean.isDebitReq()) {
				debitQuery = "SELECT agent_org_id, 0.0 'cash', 0.0 'cheque', 0.0 'cheque_bounce', IFNULL(SUM(amount),0.0) 'debit', 0.0 'credit', 0.0 'bank_deposit' FROM st_lms_bo_debit_note debit INNER JOIN st_lms_bo_transaction_master btm ON debit.transaction_id=btm.transaction_id AND debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') "+appender+" UNION ALL ";
			}
			if(requestBean.isCreditReq()) {
				creditQuery = "SELECT agent_org_id, 0.0 'cash', 0.0 'cheque', 0.0 'cheque_bounce', 0.0 'debit', IFNULL(SUM(amount),0.0) 'credit', 0.0 'bank_deposit' FROM st_lms_bo_credit_note credit INNER JOIN st_lms_bo_transaction_master btm ON credit.transaction_id=btm.transaction_id AND credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') "+appender+" UNION ALL ";
			}
			if(requestBean.isBankDepositReq()) {
				bankDepositQuery = "SELECT agent_org_id, 0.0 'cash', 0.0 'cheque', 0.0 'cheque_bounce', 0.0 'debit', 0.0 'credit', IFNULL(SUM(amount),0.0) 'bank_deposit' FROM st_lms_bo_bank_deposit_transaction bank INNER JOIN st_lms_bo_transaction_master btm ON bank.transaction_id=btm.transaction_id "+appender+" UNION ALL ";
			}

			query = query.concat(cashQuery).concat(chequeQuery).concat(chequeBounceQuery).concat(debitQuery).concat(creditQuery).concat(bankDepositQuery);
			query = query.substring(0, query.lastIndexOf(" UNION ALL "));
			query = query.concat(footer);

			System.out.println(query);

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				int agentOrgId = rs.getInt("agentOrgId");

				paymentBean = paymentReportMap.get(agentOrgId);
				if(paymentBean == null) {
					paymentBean = new PaymentReportBean();
					paymentReportMap.put(agentOrgId, paymentBean);
					paymentBean.setAgentOrgId(agentOrgId);
				}
				paymentBean.setName(rs.getString("agentName"));
				paymentBean.setCashAmt(rs.getDouble("cash"));
				paymentBean.setChequeAmt(rs.getDouble("cheque"));
				paymentBean.setChequeBounceAmt(rs.getDouble("cheque_bounce"));
				paymentBean.setDebitAmt(rs.getDouble("debit"));
				paymentBean.setCreditAmt(rs.getDouble("credit"));
				paymentBean.setBankDepositAmt(rs.getDouble("bank_deposit"));
				paymentBean.setNetPayment(rs.getDouble("net_payment"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getNetPaymentTransactionRetailer(PaymentRequestBean requestBean, Map<Integer, PaymentReportBean> paymentReportMap, Connection con) {

		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String cashQuery = "";
		String chequeQuery = "";
		String chequeBounceQuery = "";
		String debitQuery = "";
		String creditQuery = "";
		String appender = null;
		String footer = null;
		PaymentReportBean paymentBean = null;
		try {
			if(!(requestBean.isCashReq() || requestBean.isChequeReq() || requestBean.isChequeBounceReq() || requestBean.isDebitReq() || requestBean.isCreditReq()))
				throw new LMSException("Please Select Atleast one Transaction Type.");

			query = "SELECT organization_id retailerOrgId, NAME retName, IFNULL(cash, 0.0) cash, IFNULL(cheque, 0.0) cheque, IFNULL(cheque_bounce, 0.0) cheque_bounce, IFNULL(debit, 0.0) debit, IFNULL(credit, 0.0) credit, 0.0 'bank_deposit', (cash+cheque-cheque_bounce-debit+credit) AS net_payment FROM ( SELECT retailer_org_id, SUM(cash) cash, SUM(cheque) cheque, SUM(cheque_bounce) cheque_bounce, SUM(debit) debit, SUM(credit) credit FROM ( ";
			if(requestBean.getRetailerOrgId() == 0) {
				String joinType = null;
				if(requestBean.isAllDataReq()) {
					joinType = "RIGHT";
				} else {
					joinType = "INNER";
				}
				appender = "AND btm.transaction_date>='"+requestBean.getStartDate()+"' AND btm.transaction_date<='"+requestBean.getEndDate()+"' GROUP BY retailer_org_id";
				footer = ")aa GROUP BY retailer_org_id)bb "+joinType+" JOIN (SELECT organization_id, NAME FROM st_lms_organization_master slom WHERE organization_type='RETAILER')slom ON bb.retailer_org_id=slom.organization_id GROUP BY slom.organization_id;";
			} else {
				appender = "AND btm.transaction_date>='"+requestBean.getStartDate()+"' AND btm.transaction_date<='"+requestBean.getEndDate()+"' AND agent_org_id="+requestBean.getAgentOrgId()+" AND retailer_org_id="+requestBean.getRetailerOrgId();
				footer = ")aa )bb INNER JOIN st_lms_organization_master ON retailer_org_id=organization_id;";
			}

			if(requestBean.isCashReq()) {
				cashQuery = "SELECT retailer_org_id, IFNULL(SUM(cash.amount),0) 'cash', 0.0 'cheque', 0.0 'cheque_bounce', 0.0 'debit', 0.0 'credit' FROM st_lms_agent_cash_transaction cash INNER JOIN st_lms_agent_transaction_master btm ON cash.transaction_id=btm.transaction_id "+appender+" UNION ALL ";
			}
			if(requestBean.isChequeReq()) {
				chequeQuery = "SELECT retailer_org_id, 0.0 'cash', IFNULL(SUM(chq.cheque_amt),0) 'cheque', 0.0 'cheque_bounce', 0.0 'debit', 0.0 'credit' FROM st_lms_agent_sale_chq chq INNER JOIN st_lms_agent_transaction_master btm ON chq.transaction_id=btm.transaction_id AND chq.transaction_type IN ('CHEQUE', 'CLOSED') "+appender+" UNION ALL ";
			}
			if(requestBean.isChequeBounceReq()) {
				chequeBounceQuery = "SELECT retailer_org_id, 0.0 'cash', 0.0 'cheque', IFNULL(SUM(chq.cheque_amt),0) 'cheque_bounce', 0.0 'debit', 0.0 'credit' FROM st_lms_agent_sale_chq chq INNER JOIN st_lms_agent_transaction_master btm ON chq.transaction_id=btm.transaction_id AND chq.transaction_type='CHQ_BOUNCE' "+appender+" UNION ALL ";
			}
			if(requestBean.isDebitReq()) {
				debitQuery = "SELECT retailer_org_id, 0.0 'cash', 0.0 'cheque', 0.0 'cheque_bounce', IFNULL(SUM(bo.amount),0) 'debit', 0.0 'credit' FROM st_lms_agent_debit_note bo INNER JOIN st_lms_agent_transaction_master btm ON btm.transaction_id=bo.transaction_id AND bo.transaction_type IN('DR_NOTE_CASH', 'DR_NOTE') "+appender+" UNION ALL ";
			}
			if(requestBean.isCreditReq()) {
				creditQuery = "SELECT retailer_org_id, 0.0 'cash', 0.0 'cheque', 0.0 'cheque_bounce', 0.0 'debit', IFNULL(SUM(bo.amount),0) 'credit' FROM st_lms_agent_credit_note bo INNER JOIN st_lms_agent_transaction_master btm ON btm.transaction_id=bo.transaction_id AND bo.transaction_type IN('CR_NOTE_CASH', 'CR_NOTE') "+appender+" UNION ALL ";
			}

			query = query.concat(cashQuery).concat(chequeQuery).concat(chequeBounceQuery).concat(debitQuery).concat(creditQuery);
			query = query.substring(0, query.lastIndexOf(" UNION ALL "));
			query = query.concat(footer);

			System.out.println(query);

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				int retailerOrgId = rs.getInt("retailerOrgId");

				paymentBean = paymentReportMap.get(retailerOrgId);
				if(paymentBean == null) {
					paymentBean = new PaymentReportBean();
					paymentReportMap.put(retailerOrgId, paymentBean);
					paymentBean.setRetailerOrgId(retailerOrgId);
				}
				paymentBean.setName(rs.getString("retName"));
				paymentBean.setCashAmt(rs.getDouble("cash"));
				paymentBean.setChequeAmt(rs.getDouble("cheque"));
				paymentBean.setChequeBounceAmt(rs.getDouble("cheque_bounce"));
				paymentBean.setDebitAmt(rs.getDouble("debit"));
				paymentBean.setCreditAmt(rs.getDouble("credit"));
				paymentBean.setBankDepositAmt(rs.getDouble("bank_deposit"));
				paymentBean.setNetPayment(rs.getDouble("net_payment"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/lms_zim", "root", "root");

		PaymentRequestBean requestBean = new PaymentRequestBean();

		requestBean.setAgentOrgId(0);
		requestBean.setRetailerOrgId(0);
		requestBean.setStartDate("2011-05-01 00:00:00");
		requestBean.setEndDate("2013-12-12 23:59:59");
		requestBean.setCashReq(true);
		requestBean.setChequeReq(true);
		requestBean.setChequeBounceReq(true);
		requestBean.setDebitReq(true);
		requestBean.setCreditReq(true);
		requestBean.setBankDepositReq(true);
		requestBean.setAllDataReq(true);

		Map<Integer, PaymentReportBean> paymentReportMap = new HashMap<Integer, PaymentReportBean>();
		ReportDataUtility.getNetPaymentTransactionAgent(requestBean, paymentReportMap, con);
		//ReportDataUtility.getNetPaymentTransactionRetailer(requestBean, paymentReportMap, con);
		Set<Integer> set = paymentReportMap.keySet();
		for(Integer i : set) {
			System.out.println(paymentReportMap.get(i));
		}
	}
}