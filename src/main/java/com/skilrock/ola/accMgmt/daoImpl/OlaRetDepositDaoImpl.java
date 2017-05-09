package com.skilrock.ola.accMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRefundBean;



public class OlaRetDepositDaoImpl {
	
		static Log logger = LogFactory.getLog(OlaRetDepositDaoImpl.class);	
		
		public static boolean depositeRefund(long depositTxnId, UserInfoBean userBean, Connection con) throws SQLException, LMSException {
			OLADepositRefundBean refundBean = new OLADepositRefundBean();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			boolean isValid=false;
			long transactionId = 0;
			String fetchRefundDataQuery = "select wallet_id,ims_ref_transaction_id, deposit_amt, net_amt, agent_net_amt, retailer_comm, agent_comm,  party_id from st_ola_ret_deposit where transaction_id = ?";
			pstmt = con.prepareStatement(fetchRefundDataQuery);
			pstmt.setLong(1, depositTxnId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				refundBean.setWalletId(rs.getInt("wallet_id"));
				refundBean.setRefTxnId(Long.parseLong(rs.getString("ims_ref_transaction_id")));
				refundBean.setDepositAmt(rs.getDouble("deposit_amt"));
				refundBean.setRetNetAmt(rs.getDouble("net_amt"));
				refundBean.setAgtNetAmt(rs.getDouble("agent_net_amt"));
				refundBean.setRetComm(rs.getDouble("retailer_comm"));
				refundBean.setAgtComm(rs.getDouble("agent_comm"));
				refundBean.setPlrId(rs.getInt("party_id"));		
			}
			pstmt.clearParameters();
			String insertInLMS = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
			pstmt1.setString(1, "RETAILER");
			
			pstmt1.executeUpdate();
			
			ResultSet rs1 = pstmt1.getGeneratedKeys();
			if (rs1.next()) {
				transactionId = rs1.getLong(1);
				// insert into retailer transaction master
				pstmt = con.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				pstmt.setLong(1, transactionId);
				pstmt.setInt(2, userBean.getUserId());
				pstmt.setInt(3, userBean.getUserOrgId());
				pstmt.setInt(4, refundBean.getWalletId());
				pstmt.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
				pstmt.setString(6, "OLA_DEPOSIT_REFUND");
				pstmt.executeUpdate();
				pstmt.clearParameters();
				pstmt = con.prepareStatement("insert into st_ola_ret_deposit_refund(transaction_id, wallet_id, retailer_org_id, ims_ref_transaction_id, ola_ref_transaction_id, claim_status, cancel_reason, deposit_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, agent_ref_transaction_id, party_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setLong(1, transactionId);
				pstmt.setInt(2, refundBean.getWalletId());
				pstmt.setInt(3, userBean.getUserOrgId());
				pstmt.setLong(4, refundBean.getRefTxnId());
				pstmt.setLong(5, depositTxnId);
				pstmt.setString(6, "CLAIM_BAL");
				pstmt.setString(7, "CANCEL_SERVER");
				pstmt.setDouble(8, refundBean.getDepositAmt());
				pstmt.setDouble(9, refundBean.getRetNetAmt());
				pstmt.setDouble(10, refundBean.getAgtNetAmt());
				pstmt.setDouble(11, refundBean.getRetComm());
				pstmt.setDouble(12, refundBean.getAgtComm());
				pstmt.setInt(13, 0);
				pstmt.setInt(14, refundBean.getPlrId());
				pstmt.executeUpdate();
				
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(refundBean.getRetNetAmt(), "CLAIM_BAL", "DEBIT", userBean
						.getUserOrgId(), userBean
						.getParentOrgId(), "RETAILER", 0, con);
				if(!isValid){
					throw new LMSException(LMSErrors.INVALID_RETAILER_ERROR_CODE);
				}
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(refundBean.getAgtNetAmt(), "CLAIM_BAL", "DEBIT", userBean
						.getParentOrgId(),0, "AGENT", 0, con);
				if(!isValid){
					throw new LMSException(LMSErrors.INVALID_RETAILER_ERROR_CODE);
				}

			} else {
				return false;
			}
			return true;
		}		
}
