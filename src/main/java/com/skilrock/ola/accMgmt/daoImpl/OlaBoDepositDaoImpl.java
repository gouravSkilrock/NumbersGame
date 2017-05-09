package com.skilrock.ola.accMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRefundBean;



public class OlaBoDepositDaoImpl {
	
		static Log logger = LogFactory.getLog(OlaBoDepositDaoImpl.class);	
		
		public static boolean depositeRefund(long depositTxnId, UserInfoBean userBean, Connection con) throws SQLException, LMSException {
			OLADepositRefundBean refundBean = new OLADepositRefundBean();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String fetchRefundDataQuery = "select wallet_id, deposit_amt,  plr_id from st_ola_bo_direct_plr_deposit where transaction_id = ?";
			pstmt = con.prepareStatement(fetchRefundDataQuery);
			pstmt.setLong(1, depositTxnId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				refundBean.setWalletId(rs.getInt("wallet_id"));
				refundBean.setDepositAmt(rs.getDouble("deposit_amt"));
				refundBean.setPlrId(rs.getInt("plr_id"));		
			}
			pstmt.clearParameters();
			String insertInLMS = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
			pstmt1.setString(1, "BO");
			long transactionId = 0;
			pstmt1.executeUpdate();
			int isUpdate=0;
			ResultSet rs1 = pstmt1.getGeneratedKeys();
			if (rs1.next()) {
				transactionId = rs1.getLong(1);
				// insert into retailer transaction master
				pstmt1 = con.prepareStatement("INSERT INTO st_lms_bo_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
				pstmt1.setLong(1, transactionId);
				pstmt1.setInt(2, userBean.getUserId());
				pstmt1.setInt(3, userBean.getUserOrgId());
				pstmt1.setString(4, "PLAYER");	
				pstmt1.setInt(5, refundBean.getPlrId());
				pstmt1.setString(6, "OLA_DEPOSIT_REFUND_PLR");
				
				pstmt1.setTimestamp(7, Util.getCurrentTimeStamp());
			
				isUpdate = pstmt1.executeUpdate();
			
				logger.info("inserted into bo transaction master"+isUpdate);

				pstmt1 = con
						.prepareStatement("insert into st_ola_bo_direct_plr_deposit_refund(transaction_id,bo_user_id,bo_org_id,wallet_id,plr_id,deposit_amt,cancel_reason,ref_transaction_id) values(?,?,?,?,?,?,?,?)");
				pstmt1.setLong(1, transactionId);
				pstmt1.setInt(2, userBean.getUserId());
				pstmt1.setInt(3, userBean.getUserOrgId());
				
				pstmt1.setInt(4, refundBean.getWalletId());
				pstmt1.setInt(5,refundBean.getPlrId());
				pstmt1.setDouble(6, refundBean.getDepositAmt());
				pstmt1.setString(7, "CANCEL_SERVER");
				pstmt1.setLong(8, depositTxnId);
				isUpdate=pstmt1.executeUpdate();
					
				logger.info("inserted into st_ola_bo_direct_plr_deposit_refund "+isUpdate);
		
			} else {
				return false;
			}
			return true;
		}		
}
