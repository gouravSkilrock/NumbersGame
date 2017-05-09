package com.skilrock.ola.accMgmt.common;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalRequestBean;

public class AccountMgmtCommonFunction{
	
	static Log logger = LogFactory.getLog(AccountMgmtCommonFunction.class);
	
	public static Long withdrawlRequestIntiated(OLAWithdrawalRequestBean reqBean, UserInfoBean userBean,Connection con) throws SQLException, LMSException{
		// insert withdrawal details in st_ola_ret_withdrawal_temp
		long tempTransactionId=0;
		PreparedStatement insertTemp = con.prepareStatement("insert into st_ola_withdrawl_temp(wallet_id,party_id,org_id,ims_ref_transaction_id,withdrawl_amt,withdrawl_channel,status,ref_transaction_id)values(?,?,?,?,?,?,?,?)");
		insertTemp.setInt(1, reqBean.getWalletId());
		insertTemp.setInt(2, reqBean.getPlayerId());
		insertTemp.setInt(3, userBean.getUserOrgId());
		insertTemp.setInt(4, 0);
		insertTemp.setDouble(5, reqBean.getWithdrawlAmt());
		insertTemp.setString(6, reqBean.getDeviceType());
		insertTemp.setString(7, "INITIATED");
		insertTemp.setInt(8, 0);
		
		insertTemp.executeUpdate();
		ResultSet resultSet2 = insertTemp.getGeneratedKeys();
		if (resultSet2.next()) {
			tempTransactionId = resultSet2.getLong(1);		
	}else {
		throw new LMSException(LMSErrors.MONEY_WITHDRAWL_ERROR_CODE);
	}
		return tempTransactionId;
	}
	
	public static void updateWithdrawlTmpStatus(String status,long pmsTxnId,long txnId,Connection con) throws SQLException{
	
		PreparedStatement pstmt = con.prepareStatement("update st_ola_withdrawl_temp set status=?, ims_ref_transaction_id=?  where task_id=?");
			pstmt.setString(1, status);
			pstmt.setLong(2, pmsTxnId);
			pstmt.setLong(3,txnId);
		pstmt.executeUpdate();
		
	}
	
}
